package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;
import br.com.caiogs06.poo.avaliacao.quiz.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

  @Autowired
  private UsuarioService usuarioService;

  @GetMapping("/login")
  public String loginPage(HttpSession session) {
    if (session.getAttribute("usuarioId") != null)
      return "redirect:/";
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
    Usuario usuario = usuarioService.autenticar(email, senha);
    if (usuario != null) {
      session.setAttribute("usuarioId", usuario.getId());
      session.setAttribute("usuarioNome", usuario.getNome());
      session.setAttribute("usuarioEmail", usuario.getEmail());
      session.setAttribute("usuarioFotoId", usuario.getFotoId());
      return "redirect:/";
    }
    model.addAttribute("erro", "E-mail ou senha inválidos!");
    return "login";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }
}