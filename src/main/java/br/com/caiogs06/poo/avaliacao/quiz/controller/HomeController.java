package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import br.com.caiogs06.poo.avaliacao.quiz.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;
  @Autowired
  private UsuarioService usuarioService;

  @GetMapping("/")
  public String home(Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "home");
    model.addAttribute("questionarios", questionarioService.listarTodos());

    return "home";
  }

  @PostMapping("/alterar-nome")
  public String alterarNome(@RequestParam String novoNome, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    Long usuarioId = obterUsuarioId(session);
    usuarioService.atualizarNome(usuarioId, novoNome);
    session.setAttribute("usuarioNome", novoNome);

    return "redirect:/";
  }

  @PostMapping("/alterar-foto")
  public String alterarFoto(@RequestParam Integer novaFoto, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    Long usuarioId = obterUsuarioId(session);
    usuarioService.atualizarFoto(usuarioId, novaFoto);
    session.setAttribute("usuarioFotoId", novaFoto);

    return "redirect:/";
  }
}