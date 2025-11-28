package br.com.caiogs06.poo.avaliacao.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;

@Repository
public class UsuarioDAO {

  @Autowired
  private JdbcTemplate jdbc;

  @NonNull
  private final RowMapper<Usuario> rowMapper = (rs, rowNum) -> {
    return new Usuario(
        rs.getLong("id"),
        rs.getString("nome"),
        rs.getString("email"),
        rs.getString("senha"),
        (Integer) rs.getObject("foto_id"),
        rs.getObject("data_criacao", LocalDateTime.class));
  };

  public Usuario buscarPorEmail(String email) {
    String sql = "SELECT id, nome, email, senha, foto_id, data_criacao FROM usuario WHERE email = ?";
    List<Usuario> usuarios = jdbc.query(sql, rowMapper, email);

    return usuarios.isEmpty() ? null : usuarios.get(0);
  }

  public Usuario buscarPorId(Long id) {
    String sql = "SELECT id, nome, email, senha, foto_id, data_criacao FROM usuario WHERE id = ?";
    List<Usuario> usuarios = jdbc.query(sql, rowMapper, id);

    return usuarios.isEmpty() ? null : usuarios.get(0);
  }

  public Long salvar(Usuario usuario) {
    String sql = "INSERT INTO usuario (nome, email, senha, foto_id) VALUES (?, ?, ?, ?) RETURNING id";
    return jdbc.queryForObject(sql, Long.class, usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
        usuario.getFotoId());
  }

  public void atualizarNome(Long id, String novoNome) {
    String sql = "UPDATE usuario SET nome = ? WHERE id = ?";
    jdbc.update(sql, novoNome, id);
  }

  public void atualizarFoto(Long id, Integer novaFotoId) {
    String sql = "UPDATE usuario SET foto_id = ? WHERE id = ?";
    jdbc.update(sql, novaFotoId, id);
  }

  public void atualizarSenha(Long id, String novaSenha) {
    String sql = "UPDATE usuario SET senha = ? WHERE id = ?";
    jdbc.update(sql, novaSenha, id);
  }
}