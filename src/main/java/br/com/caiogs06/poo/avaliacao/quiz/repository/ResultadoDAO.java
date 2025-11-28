package br.com.caiogs06.poo.avaliacao.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import br.com.caiogs06.poo.avaliacao.quiz.model.ResultadoQuestionario;

@Repository
public class ResultadoDAO {

  @Autowired
  private JdbcTemplate jdbc;

  @NonNull
  private final RowMapper<ResultadoQuestionario> rowMapper = (rs, rowNum) -> {
    return new ResultadoQuestionario(
        rs.getLong("id"),
        rs.getLong("questionario_id"),
        rs.getLong("respondente_id"),
        (Double) rs.getObject("nota_final"),
        rs.getObject("data_submissao", LocalDateTime.class));
  };

  public List<ResultadoQuestionario> listarPorRespondente(Long respondenteId) {
    String sql = "SELECT id, questionario_id, respondente_id, nota_final, data_submissao FROM resultado_questionario WHERE respondente_id = ? ORDER BY data_submissao DESC";
    return jdbc.query(sql, rowMapper, respondenteId);
  }

  public ResultadoQuestionario buscarPorId(Long id) {
    String sql = "SELECT id, questionario_id, respondente_id, nota_final, data_submissao FROM resultado_questionario WHERE id = ?";
    List<ResultadoQuestionario> lista = jdbc.query(sql, rowMapper, id);
    return lista.isEmpty() ? null : lista.get(0);
  }

  public ResultadoQuestionario buscarPorQuestionarioERespondente(Long questionarioId, Long respondenteId) {
    String sql = "SELECT id, questionario_id, respondente_id, nota_final, data_submissao FROM resultado_questionario WHERE questionario_id = ? AND respondente_id = ?";
    List<ResultadoQuestionario> lista = jdbc.query(sql, rowMapper, questionarioId, respondenteId);
    return lista.isEmpty() ? null : lista.get(0);
  }

  public Long salvar(ResultadoQuestionario res) {
    String sql = "INSERT INTO resultado_questionario (questionario_id, respondente_id, nota_final) VALUES (?, ?, ?) RETURNING id";
    return jdbc.queryForObject(sql, Long.class,
        res.getQuestionarioId(),
        res.getRespondenteId(),
        res.getNotaFinal());
  }

  public void deletar(Long id) {
    jdbc.update("DELETE FROM resultado_questionario WHERE id = ?", id);
  }
}