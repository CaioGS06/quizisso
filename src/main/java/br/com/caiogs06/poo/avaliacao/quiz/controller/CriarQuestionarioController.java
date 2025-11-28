package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class CriarQuestionarioController extends BaseController {

  @GetMapping("/criar-questionario")
  public String paginaCriar(Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "criar-questionario");
    // A lógica de POST para criar itens geralmente envolve AJAX ou passos
    // múltiplos,
    // mantive apenas a view inicial aqui para simplificar.
    return "criar-questionario";
  }
}