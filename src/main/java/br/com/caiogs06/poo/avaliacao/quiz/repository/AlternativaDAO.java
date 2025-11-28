package br.com.caiogs06.poo.avaliacao.quiz.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import br.com.caiogs06.poo.avaliacao.quiz.model.Alternativa;

@Repository
public class AlternativaDAO {

  @Autowired
  private JdbcTemplate jdbc;

  @NonNull
  private final RowMapper<Alternativa> rowMapper = (rs, rowNum) -> {
    return new Alternativa(
        rs.getLong("id"),
        rs.getString("descricao"),
        rs.getBoolean("esta_correta"),
        rs.getInt("ordem"));
  };

  public List<Alternativa> listarPorItem(Long itemId) {
    String sql = "SELECT id, descricao, esta_correta, ordem FROM alternativa WHERE item_id = ? ORDER BY ordem";
    return jdbc.query(sql, rowMapper, itemId);
  }

  public Alternativa buscarPorId(Long id) {
    String sql = "SELECT id, descricao, esta_correta, ordem FROM alternativa WHERE id = ?";
    List<Alternativa> result = jdbc.query(sql, rowMapper, id);
    return result.isEmpty() ? null : result.get(0);
  }

  public Long salvar(Alternativa alternativa, Long itemId) {
    String sql = "INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES (?, ?, ?, ?) RETURNING id";
    return jdbc.queryForObject(sql, Long.class, itemId, alternativa.getDescricao(),
        alternativa.getEstaCorreta(), alternativa.getOrdem());
  }

  public void atualizar(Alternativa alternativa) {
    String sql = "UPDATE alternativa SET descricao = ?, esta_correta = ?, ordem = ? WHERE id = ?";
    jdbc.update(sql, alternativa.getDescricao(), alternativa.getEstaCorreta(),
        alternativa.getOrdem(), alternativa.getId());
  }

  public void deletar(Long id) {
    String sql = "DELETE FROM alternativa WHERE id = ?";
    jdbc.update(sql, id);
  }

  public void deletarPorItem(Long itemId) {
    String sql = "DELETE FROM alternativa WHERE item_id = ?";
    jdbc.update(sql, itemId);
  }
}