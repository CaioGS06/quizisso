package br.com.caiogs06.poo.avaliacao.quiz.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.caiogs06.poo.avaliacao.quiz.model.Alternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.Imagens;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoDissertativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.service.ItemService;
import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CriarQuestionarioController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;

  @Autowired
  private ItemService itemService;

  @GetMapping("/criar-questionario")
  public String paginaCriar(Model model, HttpSession session) {
    if (!estaAutenticado(session))
      return "redirect:/login";

    adicionarDadosUsuario(model, session);
    model.addAttribute("paginaAtual", "criar-questionario");
    model.addAttribute("banners", Imagens.obterBanners());
    return "criar-questionario";
  }

  @PostMapping("/criar-questionario")
  public ResponseEntity<?> criar(@RequestBody Map<String, Object> dados, HttpSession session) {
    try {
      if (!estaAutenticado(session)) {
        return ResponseEntity.status(401).body(Map.of("erro", "Usuário não autenticado"));
      }

      Long criadorId = obterUsuarioId(session);
      String titulo = (String) dados.get("titulo");
      Integer bannerId = (Integer) dados.get("bannerId");
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> questoes = (List<Map<String, Object>>) dados.get("questoes");

      // Validações básicas
      if (titulo == null || titulo.trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Título é obrigatório"));
      }

      if (questoes == null || questoes.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Adicione pelo menos uma questão"));
      }

      // Criar questionário (sem validação de itens ainda)
      Questionario questionario = new Questionario(null, titulo, null, criadorId, bannerId != null ? bannerId : 1,
          LocalDateTime.now());

      // Validação manual (sem exigir itens neste ponto)
      if (titulo == null || titulo.trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Título é obrigatório"));
      }

      // Salvar questionário e obter ID
      Long questionarioId = questionarioService.salvar(questionario);
      questionario.setId(questionarioId);

      // Criar itens (questões)
      for (Map<String, Object> questaoData : questoes) {
        String tipo = (String) questaoData.get("tipo");
        String enunciado = (String) questaoData.get("enunciado");
        Integer ordem = (Integer) questaoData.get("ordem");

        if ("alternativa".equalsIgnoreCase(tipo)) {
          @SuppressWarnings("unchecked")
          List<Map<String, Object>> alternativasData = (List<Map<String, Object>>) questaoData.get("alternativas");

          // Criar questão de alternativa
          QuestaoAlternativa questao = new QuestaoAlternativa(null, enunciado, 1.0, ordem);
          Long itemId = itemService.salvar(questao, questionarioId);

          // Salvar alternativas
          if (alternativasData != null) {
            questao = new QuestaoAlternativa(itemId, enunciado, 1.0, ordem);
            for (Map<String, Object> altData : alternativasData) {
              Integer ordemAlt = (Integer) altData.get("ordem");
              String descricao = (String) altData.get("descricao");
              Boolean correta = (Boolean) altData.get("correta");

              Alternativa alt = new Alternativa(null, descricao, correta != null && correta, ordemAlt);
              questao.getAlternativas().add(alt);
            }
            // Atualizar questão com alternativas
            itemService.atualizar(questao);
          }
        } else {
          // Questão dissertativa
          String gabarito = (String) questaoData.get("gabarito");
          QuestaoDissertativa questao = new QuestaoDissertativa(null, enunciado, gabarito, 1.0, ordem);
          itemService.salvar(questao, questionarioId);
        }
      }

      Map<String, Object> resposta = new HashMap<>();
      resposta.put("sucesso", true);
      resposta.put("mensagem", "Questionário criado com sucesso!");
      resposta.put("questionarioId", questionarioId);

      return ResponseEntity.ok(resposta);

    } catch (Exception e) {
      Map<String, Object> erro = new HashMap<>();
      erro.put("sucesso", false);
      erro.put("erro", "Erro ao criar questionário: " + e.getMessage());
      return ResponseEntity.badRequest().body(erro);
    }
  }
}