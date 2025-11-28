package br.com.caiogs06.poo.avaliacao.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;
import br.com.caiogs06.poo.avaliacao.quiz.repository.UsuarioDAO;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioDAO usuarioDAO;

  public Usuario autenticar(String email, String senha) {
    Usuario usuario = usuarioDAO.buscarPorEmail(email);
    if (usuario != null && usuario.getSenha().equals(senha)) {
      return usuario; // Sucesso
    }
    return null; // Falha
  }

  public Usuario buscarPorId(Long id) {
    return usuarioDAO.buscarPorId(id);
  }

  @Transactional
  public Long cadastrarUsuario(Usuario usuario) {
    if (usuario == null)
      throw new IllegalArgumentException("Usuário não pode ser nulo");

    Usuario existente = usuarioDAO.buscarPorEmail(usuario.getEmail());
    if (existente != null) {
      throw new IllegalStateException("E-mail já cadastrado: " + usuario.getEmail());
    }

    return usuarioDAO.salvar(usuario);
  }

  @Transactional
  public void atualizarNome(Long id, String novoNome) {
    if (novoNome == null || novoNome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome inválido");
    }
    usuarioDAO.atualizarNome(id, novoNome);
  }

  @Transactional
  public void atualizarFoto(Long id, Integer novaFotoId) {
    usuarioDAO.atualizarFoto(id, novaFotoId);
  }

  @Transactional
  public void alterarSenha(Long id, String senhaAntiga, String senhaNova) {
    Usuario usuario = usuarioDAO.buscarPorId(id);
    if (usuario == null)
      throw new IllegalArgumentException("Usuário não encontrado");

    // A lógica de validação fica no domínio
    usuario.alterarSenha(senhaAntiga, senhaNova);

    usuarioDAO.atualizarSenha(id, senhaNova);
  }
}