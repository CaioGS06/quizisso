package br.com.caiogs06.poo.avaliacao.quiz.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import br.com.caiogs06.poo.avaliacao.quiz.model.Item;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoDissertativa;

@Repository
public class ItemDAO {

  @Autowired
  private JdbcTemplate jdbc;

  @NonNull
  private final RowMapper<Item<?>> rowMapper = (rs, rowNum) -> {
    String tipo = rs.getString("tipo");
    if (tipo.equals("DISSERTATIVA")) {
      return new QuestaoDissertativa(
          rs.getLong("id"),
          rs.getString("enunciado"),
          rs.getString("gabarito"),
          rs.getDouble("pontuacao_maxima"),
          rs.getInt("ordem"));
    } else {
      return new QuestaoAlternativa(
          rs.getLong("id"),
          rs.getString("enunciado"),
          rs.getDouble("pontuacao_maxima"),
          rs.getInt("ordem"));
    }
  };

  public List<Item<?>> listarPorQuestionario(Long questionarioId) {
    String sql = "SELECT id, questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem FROM item WHERE questionario_id = ? ORDER BY ordem";
    return jdbc.query(sql, rowMapper, questionarioId);
  }

  public Item<?> buscarPorId(Long id) {
    String sql = "SELECT id, questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem FROM item WHERE id = ?";
    List<Item<?>> result = jdbc.query(sql, rowMapper, id);
    return result.isEmpty() ? null : result.get(0);
  }

  public Long salvar(Item<?> item, Long questionarioId) {
    String sql;
    Long idGerado;

    switch (item) {
      case QuestaoDissertativa qd -> {
        sql = "INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES (?, 'DISSERTATIVA', ?, ?, ?, ?) RETURNING id";
        idGerado = jdbc.queryForObject(sql, Long.class, questionarioId, qd.getEnunciado(), qd.getGabarito(),
            qd.getPontuacaoMaxima(), qd.getOrdem());
      }
      case QuestaoAlternativa qa -> {
        sql = "INSERT INTO item (questionario_id, tipo, enunciado, pontuacao_maxima, ordem) VALUES (?, 'ALTERNATIVA', ?, ?, ?) RETURNING id";
        idGerado = jdbc.queryForObject(sql, Long.class, questionarioId, qa.getEnunciado(), qa.getPontuacaoMaxima(),
            qa.getOrdem());
      }
      default -> throw new IllegalArgumentException("Tipo de item desconhecido");
    }
    return idGerado;
  }

  public void atualizar(Item<?> item) {
    switch (item) {
      case QuestaoDissertativa qd -> {
        String sql = "UPDATE item SET enunciado = ?, gabarito = ?, pontuacao_maxima = ?, ordem = ? WHERE id = ?";
        jdbc.update(sql, qd.getEnunciado(), qd.getGabarito(), qd.getPontuacaoMaxima(), qd.getOrdem(), qd.getId());
      }
      case QuestaoAlternativa qa -> {
        String sql = "UPDATE item SET enunciado = ?, pontuacao_maxima = ?, ordem = ? WHERE id = ?";
        jdbc.update(sql, qa.getEnunciado(), qa.getPontuacaoMaxima(), qa.getOrdem(), qa.getId());
      }
      default -> {
        throw new IllegalArgumentException("Tipo de item desconhecido");
      }
    }
  }

  public void deletar(Long id) {
    jdbc.update("DELETE FROM item WHERE id = ?", id);
  }
}