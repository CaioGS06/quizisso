package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.caiogs06.poo.avaliacao.quiz.service.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class QuestionariosRespondidosController extends BaseController {

  @Autowired
  private ResultadoService resultadoService;

  @GetMapping("/questionarios-respondidos")
  public String listar(Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "questionarios-respondidos");

    Long usuarioId = obterUsuarioId(session);
    model.addAttribute("resultados", resultadoService.listarPorRespondente(usuarioId));

    return "questionarios-respondidos";
  }
}