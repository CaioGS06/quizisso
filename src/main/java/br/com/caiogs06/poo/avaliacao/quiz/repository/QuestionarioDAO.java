package br.com.caiogs06.poo.avaliacao.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;

@Repository
public class QuestionarioDAO {

  @Autowired
  private JdbcTemplate jdbc;

  @NonNull
  private final RowMapper<Questionario> rowMapper = (rs, rowNum) -> {
    return new Questionario(
        rs.getLong("id"),
        rs.getString("titulo"),
        rs.getString("descricao"),
        rs.getLong("criador_id"),
        (Integer) rs.getObject("banner_id"),
        rs.getObject("data_criacao", LocalDateTime.class));
  };

  public List<Questionario> listarTodos() {
    String sql = "SELECT id, titulo, descricao, criador_id, banner_id, data_criacao FROM questionario ORDER BY data_criacao DESC";
    List<Questionario> lista = jdbc.query(sql, rowMapper);
    return lista;
  }

  public List<Questionario> listarPorCriador(Long criadorId) {
    String sql = "SELECT id, titulo, descricao, criador_id, banner_id, data_criacao FROM questionario WHERE criador_id = ? ORDER BY data_criacao DESC";
    List<Questionario> lista = jdbc.query(sql, rowMapper, criadorId);
    return lista;
  }

  public Questionario buscarPorId(Long id) {
    String sql = "SELECT id, titulo, descricao, criador_id, banner_id, data_criacao FROM questionario WHERE id = ?";
    List<Questionario> result = jdbc.query(sql, rowMapper, id);
    return result.isEmpty() ? null : result.get(0);
  }

  public Long salvar(Questionario q) {
    String sql = "INSERT INTO questionario (titulo, descricao, criador_id, banner_id, data_criacao) VALUES (?, ?, ?, ?, ?) RETURNING id";
    return jdbc.queryForObject(sql, Long.class, q.getTitulo(), q.getDescricao(), q.getCriadorId(), q.getBannerId(),
        q.getDataCriacao());
  }

  public void atualizar(Questionario q) {
    String sql = "UPDATE questionario SET titulo = ?, descricao = ?, banner_id = ? WHERE id = ?";
    jdbc.update(sql, q.getTitulo(), q.getDescricao(), q.getBannerId(), q.getId());
  }

  public void deletar(Long id) {
    String sql = "DELETE FROM questionario WHERE id = ?";
    jdbc.update(sql, id);
  }
}