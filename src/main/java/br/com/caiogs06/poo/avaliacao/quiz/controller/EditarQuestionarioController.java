package br.com.caiogs06.poo.avaliacao.quiz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caiogs06.poo.avaliacao.quiz.model.Alternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.Imagens;
import br.com.caiogs06.poo.avaliacao.quiz.model.Item;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoDissertativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;
import br.com.caiogs06.poo.avaliacao.quiz.service.ItemService;
import br.com.caiogs06.poo.avaliacao.quiz.service.QuestionarioService;
import br.com.caiogs06.poo.avaliacao.quiz.service.ResultadoService;
import br.com.caiogs06.poo.avaliacao.quiz.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EditarQuestionarioController extends BaseController {

  @Autowired
  private QuestionarioService questionarioService;

  @Autowired
  private ItemService itemService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private ResultadoService resultadoService;

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
    model.addAttribute("banners", Imagens.obterBanners());

    return "editar-questionario";
  }

  @PostMapping("/editar-questionario")
  public ResponseEntity<?> atualizar(@RequestBody Map<String, Object> dados, HttpSession session) {
    try {
      if (!estaAutenticado(session)) {
        return ResponseEntity.status(401).body(Map.of("erro", "Usuário não autenticado"));
      }

      Long questionarioId = ((Number) dados.get("id")).longValue();
      Long usuarioId = obterUsuarioId(session);
      Usuario usuario = usuarioService.buscarPorId(usuarioId);

      Questionario questionario = questionarioService.buscarPorId(questionarioId);

      if (questionario == null) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Questionário não encontrado"));
      }

      // Verificar permissão
      if (!questionario.podeSerEditadoPor(usuario)) {
        return ResponseEntity.status(403).body(Map.of("erro", "Sem permissão para editar este questionário"));
      }

      // Verificar se o questionário já possui resultados
      Boolean confirmarExclusao = (Boolean) dados.get("confirmarExclusao");
      if (resultadoService.existemResultadosPorQuestionario(questionarioId)) {
        if (confirmarExclusao == null || !confirmarExclusao) {
          // Retorna indicando que há resultados e precisa de confirmação
          return ResponseEntity.ok(Map.of(
            "sucesso", false,
            "requerConfirmacao", true,
            "mensagem", "Este questionário possui respostas de usuários. Ao editá-lo, todas as respostas e resultados serão excluídos. Deseja continuar?"
          ));
        }
        // Se confirmado, deletar resultados e respostas
        resultadoService.deletarPorQuestionario(questionarioId);
      }

      String titulo = (String) dados.get("titulo");
      Integer bannerId = (Integer) dados.get("bannerId");
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> questoes = (List<Map<String, Object>>) dados.get("questoes");

      // Atualizar metadados do questionário
      Questionario questionarioAtualizado = new Questionario(questionarioId, titulo, null,
          questionario.getCriadorId(), bannerId != null ? bannerId : questionario.getBannerId(),
          questionario.getDataCriacao());
      questionarioAtualizado.setCriador(questionario.getCriador());
      questionarioService.atualizarMetadados(questionarioAtualizado, usuario);

      // Remover todos os itens antigos
      List<Item<?>> itensAntigos = itemService.listarPorQuestionario(questionarioId);
      for (Item<?> item : itensAntigos) {
        itemService.deletar(item.getId());
      }

      // Criar novos itens
      if (questoes != null) {
        for (Map<String, Object> questaoData : questoes) {
          String tipo = (String) questaoData.get("tipo");
          String enunciado = (String) questaoData.get("enunciado");
          Integer ordem = (Integer) questaoData.get("ordem");

          if ("alternativa".equalsIgnoreCase(tipo)) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> alternativasData = (List<Map<String, Object>>) questaoData
                .get("alternativas");

            QuestaoAlternativa questao = new QuestaoAlternativa(null, enunciado, 1.0, ordem);
            Long itemId = itemService.salvar(questao, questionarioId);

            if (alternativasData != null) {
              questao = new QuestaoAlternativa(itemId, enunciado, 1.0, ordem);
              for (Map<String, Object> altData : alternativasData) {
                Integer ordemAlt = (Integer) altData.get("ordem");
                String descricao = (String) altData.get("descricao");
                Boolean correta = (Boolean) altData.get("correta");

                Alternativa alt = new Alternativa(null, descricao, correta != null && correta, ordemAlt);
                questao.getAlternativas().add(alt);
              }
              itemService.atualizar(questao);
            }
          } else {
            String gabarito = (String) questaoData.get("gabarito");
            QuestaoDissertativa questao = new QuestaoDissertativa(null, enunciado, gabarito, 1.0, ordem);
            itemService.salvar(questao, questionarioId);
          }
        }
      }

      Map<String, Object> resposta = new HashMap<>();
      resposta.put("sucesso", true);
      resposta.put("mensagem", "Questionário atualizado com sucesso!");
      resposta.put("questionarioId", questionarioId);

      return ResponseEntity.ok(resposta);

    } catch (IllegalStateException e) {
      return ResponseEntity.status(403).body(Map.of("erro", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao atualizar questionário: " + e.getMessage()));
    }
  }

  @DeleteMapping("/excluir-questionario")
  public ResponseEntity<?> excluir(@RequestParam Long id, HttpSession session) {
    try {
      if (!estaAutenticado(session)) {
        return ResponseEntity.status(401).body(Map.of("erro", "Usuário não autenticado"));
      }

      Long usuarioId = obterUsuarioId(session);
      Usuario usuario = usuarioService.buscarPorId(usuarioId);

      questionarioService.apagarQuestionario(id, usuario);

      return ResponseEntity.ok(Map.of("sucesso", true, "mensagem", "Questionário excluído com sucesso!"));

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(403).body(Map.of("erro", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao excluir questionário: " + e.getMessage()));
    }
  }
}