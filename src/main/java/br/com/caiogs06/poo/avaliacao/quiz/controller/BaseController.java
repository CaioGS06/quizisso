package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.ui.Model;

import br.com.caiogs06.poo.avaliacao.quiz.model.Imagens;
import jakarta.servlet.http.HttpSession;

public abstract class BaseController {

  protected boolean estaAutenticado(HttpSession session) {
    return session.getAttribute("usuarioId") != null;
  }

  protected Long obterUsuarioId(HttpSession session) {
    return (Long) session.getAttribute("usuarioId");
  }

  protected void adicionarDadosUsuario(Model model, HttpSession session) {
    model.addAttribute("usuarioNome", session.getAttribute("usuarioNome"));
    model.addAttribute("usuarioEmail", session.getAttribute("usuarioEmail"));
    String foto = Imagens.porId((Integer) session.getAttribute("usuarioFotoId")).getNomeArquivo();
    model.addAttribute("usuarioFoto", foto);
    model.addAttribute("fotosDisponiveis", Imagens.obterFotosUsuarios());
  }
}