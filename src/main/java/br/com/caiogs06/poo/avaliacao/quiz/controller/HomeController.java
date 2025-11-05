package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("paginaAtual", "home");
    return "home";
  }

  @GetMapping("/meus-questionarios")
  public String meusQuestionarios(Model model) {
    model.addAttribute("paginaAtual", "meus-questionarios");
    return "meus-questionarios";
  }

  @GetMapping("/questionarios-respondidos")
  public String questionariosRespondidos(Model model) {
    model.addAttribute("paginaAtual", "questionarios-respondidos");
    return "questionarios-respondidos";
  }

  @GetMapping("/responder-questionario")
  public String responderQuestionario() {
    return "responder-questionario";
  }

  @GetMapping("/editar-questionario")
  public String editarQuestionario() {
    return "editar-questionario";
  }

  @GetMapping("/visualizar-resultado")
  public String visualizarResultado() {
    return "visualizar-resultado";
  }

  @GetMapping("/criar-questionario")
  public String criarQuestionario() {
    return "criar-questionario";
  }

  @GetMapping("/logout")
  public String deslogar() {
    return "redirect:/";
  }

  @GetMapping("/alterar-foto")
  public String alterarFoto() {
    return "redirect:/";
  }

  @GetMapping("/alterar-nome")
  public String alterarNome() {
    return "redirect:/";
  }
}
