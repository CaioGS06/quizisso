package br.com.caiogs06.poo.avaliacao.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.ResultadoQuestionario;
import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import br.com.caiogs06.poo.avaliacao.quiz.service.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class VisualizarResultadosController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;

  @Autowired
  private ResultadoService resultadoService;

  @GetMapping("/visualizar-resultados")
  public String visualizarResultados(@RequestParam Long id, Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    Questionario questionario = questionarioService.buscarPorId(id);

    // Segurança: Só pode visualizar resultados de questionários que criou
    if (questionario == null || !questionario.getCriador().getId().equals(obterUsuarioId(session))) {
      return "redirect:/meus-questionarios";
    }

    List<ResultadoQuestionario> resultados = resultadoService.listarPorQuestionario(id);

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "meus-questionarios");
    model.addAttribute("questionario", questionario);
    model.addAttribute("resultados", resultados);

    return "visualizar-resultados";
  }
}
