package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class MeusQuestionariosController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;

  @GetMapping("/meus-questionarios")
  public String listarMeus(Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "meus-questionarios");

    Long usuarioId = obterUsuarioId(session);
    model.addAttribute("questionarios", questionarioService.listarPorCriador(usuarioId));

    return "meus-questionarios";
  }
}