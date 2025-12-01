package br.com.caiogs06.poo.avaliacao.quiz.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import br.com.caiogs06.poo.avaliacao.quiz.model.Resposta;
import br.com.caiogs06.poo.avaliacao.quiz.model.RespostaAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.RespostaDissertativa;

@Repository
public class RespostaDAO {

  @Autowired
  private JdbcTemplate jdbc;

  public List<Resposta> listarPorResultado(Long resultadoId) {
    String sql = "SELECT id, item_id, resultado_id, tipo, texto_resposta, alternativa_id, pontuacao_obtida FROM resposta WHERE resultado_id = ? ORDER BY item_id";
    return jdbc.query(sql, new RespostaRowMapper(), resultadoId);
  }

  public Resposta buscarPorId(Long id) {
    String sql = "SELECT id, item_id, resultado_id, tipo, texto_resposta, alternativa_id, pontuacao_obtida FROM resposta WHERE id = ?";
    List<Resposta> lista = jdbc.query(sql, new RespostaRowMapper(), id);
    return lista.isEmpty() ? null : lista.get(0);
  }

  public Long salvar(Resposta resposta, Long resultadoId) {
    switch (resposta) {
      case RespostaDissertativa rd -> {
        String sql = "INSERT INTO resposta (resultado_id, item_id, tipo, texto_resposta, pontuacao_obtida) VALUES (?, ?, 'DISSERTATIVA', ?, ?) RETURNING id";
        return jdbc.queryForObject(sql, Long.class, resultadoId, resposta.getItemId(), rd.getTextoResposta(),
            rd.getPontuacaoObtida());
      }
      case RespostaAlternativa ra -> {
        String sql = "INSERT INTO resposta (resultado_id, item_id, tipo, alternativa_id, pontuacao_obtida) VALUES (?, ?, 'ALTERNATIVA', ?, ?) RETURNING id";
        return jdbc.queryForObject(sql, Long.class, resultadoId, resposta.getItemId(), ra.getAlternativaId(),
            ra.getPontuacaoObtida());
      }
      default -> {
      }
    }
    throw new IllegalArgumentException("Tipo de resposta desconhecido");
  }

  public void atualizar(Resposta resposta) {
    switch (resposta) {
      case RespostaDissertativa rd ->
        jdbc.update("UPDATE resposta SET texto_resposta = ? WHERE id = ?", rd.getTextoResposta(), resposta.getId());
      case RespostaAlternativa ra ->
        jdbc.update("UPDATE resposta SET alternativa_id = ? WHERE id = ?", ra.getAlternativaId(), resposta.getId());
      default -> {
      }
    }
  }

  public void atualizarPontuacao(Long respostaId, Double novaPontuacao) {
    jdbc.update("UPDATE resposta SET pontuacao_obtida = ? WHERE id = ?", novaPontuacao, respostaId);
  }

  public void deletar(Long id) {
    jdbc.update("DELETE FROM resposta WHERE id = ?", id);
  }

  public void deletarPorResultado(Long resultadoId) {
    jdbc.update("DELETE FROM resposta WHERE resultado_id = ?", resultadoId);
  }

  private static class RespostaRowMapper implements RowMapper<Resposta> {
    @Override
    public Resposta mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
      String tipo = rs.getString("tipo");
      if ("DISSERTATIVA".equals(tipo)) {
        return new RespostaDissertativa(
            rs.getLong("id"),
            rs.getLong("item_id"),
            rs.getLong("resultado_id"),
            rs.getString("texto_resposta"),
            rs.getDouble("pontuacao_obtida"));
      } else {
        return new RespostaAlternativa(
            rs.getLong("id"),
            rs.getLong("item_id"),
            rs.getLong("resultado_id"),
            rs.getLong("alternativa_id"),
            rs.getDouble("pontuacao_obtida"));
      }
    }
  }
}