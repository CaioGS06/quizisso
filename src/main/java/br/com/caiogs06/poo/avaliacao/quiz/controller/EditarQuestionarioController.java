package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EditarQuestionarioController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;

  @GetMapping("/editar-questionario")
  public String editar(@RequestParam Long id, Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    Questionario q = questionarioService.buscarPorId(id);

    // Validação de propriedade
    if (q == null || !q.getCriador().getId().equals(obterUsuarioId(session))) {
      return "redirect:/meus-questionarios";
    }

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "editar-questionario");
    model.addAttribute("questionario", q);

    return "editar-questionario";
  }
}