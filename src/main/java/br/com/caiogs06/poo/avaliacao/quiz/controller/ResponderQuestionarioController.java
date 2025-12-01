package br.com.caiogs06.poo.avaliacao.quiz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caiogs06.poo.avaliacao.quiz.model.Item;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.Resposta;
import br.com.caiogs06.poo.avaliacao.quiz.model.RespostaAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.RespostaDissertativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.ResultadoQuestionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;
import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import br.com.caiogs06.poo.avaliacao.quiz.service.ResultadoService;
import br.com.caiogs06.poo.avaliacao.quiz.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ResponderQuestionarioController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;
  @Autowired
  private ResultadoService resultadoService;
  @Autowired
  private UsuarioService usuarioService;

  @GetMapping("/responder-questionario")
  public String paginaResponder(@RequestParam Long id, Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    Questionario questionario = questionarioService.buscarPorId(id);
    if (questionario == null)
      return "redirect:/";

    // Verifica se já respondeu
    Usuario usuario = usuarioService.buscarPorId(obterUsuarioId(session));
    ResultadoQuestionario resultadoExistente = resultadoService.buscarResultado(questionario, usuario);
    if (resultadoExistente != null) {
      return "redirect:/visualizar-resultado?id=" + resultadoExistente.getId();
    }

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "responder-questionario");
    model.addAttribute("questionario", questionario);
    return "responder-questionario";
  }

  @PostMapping("/responder-questionario")
  public String submeter(@RequestParam Long id,
      @RequestParam Map<String, String> params,
      HttpSession session,
      RedirectAttributes redirect) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    try {
      Usuario usuario = usuarioService.buscarPorId(obterUsuarioId(session));
      Questionario questionario = questionarioService.buscarPorId(id);

      ResultadoQuestionario resultado = new ResultadoQuestionario(questionario, usuario);
      List<Item<?>> itens = questionario.getListaItens();

      // Processa cada questão
      for (int i = 0; i < itens.size(); i++) {
        Item<?> item = itens.get(i);
        String respostaRaw = params.get("questao" + (i + 1)); // HTML deve enviar name="questao1", "questao2"...

        if (respostaRaw == null || respostaRaw.isBlank()) {
          throw new IllegalArgumentException("Responda todas as questões.");
        }

        Resposta resposta;
        if (item instanceof QuestaoAlternativa) {
          int idx = Integer.parseInt(respostaRaw);
          Long alternativaId = ((QuestaoAlternativa) item).getAlternativas().get(idx).getId();
          resposta = new RespostaAlternativa(item.getId(), alternativaId);
        } else {
          resposta = new RespostaDissertativa(item.getId(), respostaRaw);
        }
        resultado.adicionarResposta(resposta);
      }

      Long resultadoId = resultadoService.submeterRespostas(resultado);
      redirect.addFlashAttribute("sucesso", "Enviado com sucesso!");
      return "redirect:/visualizar-resultado?id=" + resultadoId;

    } catch (Exception e) {
      redirect.addFlashAttribute("erro", "Erro ao enviar: " + e.getMessage());
      return "redirect:/responder-questionario?id=" + id;
    }
  }
}