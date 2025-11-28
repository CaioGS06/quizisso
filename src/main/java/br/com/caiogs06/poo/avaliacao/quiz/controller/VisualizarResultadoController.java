package br.com.caiogs06.poo.avaliacao.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caiogs06.poo.avaliacao.quiz.model.ResultadoQuestionario;
import br.com.caiogs06.poo.avaliacao.quiz.service.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class VisualizarResultadoController extends BaseController {

  @Autowired
  private ResultadoService resultadoService;

  @GetMapping("/visualizar-resultado")
  public String visualizar(@RequestParam Long id, Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    ResultadoQuestionario resultado = resultadoService.buscarPorId(id);

    // Segurança: Só ver seu próprio resultado
    if (resultado == null || !resultado.getRespondente().getId().equals(obterUsuarioId(session))) {
      return "redirect:/questionarios-respondidos";
    }

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "visualizar-resultado");
    model.addAttribute("resultado", resultado);

    return "visualizar-resultado";
  }
}