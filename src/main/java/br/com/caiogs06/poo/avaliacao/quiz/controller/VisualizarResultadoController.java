package br.com.caiogs06.poo.avaliacao.quiz.controller;

import java.util.HashMap;
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

import br.com.caiogs06.poo.avaliacao.quiz.model.ResultadoQuestionario;
import br.com.caiogs06.poo.avaliacao.quiz.service.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class VisualizarResultadoController extends BaseController {

  @Autowired
  private ResultadoService resultadoService;

  @GetMapping("/visualizar-resultado")
  public String visualizar(
      @RequestParam Long id,
      @RequestParam(required = false, defaultValue = "false") Boolean correcao,
      Model model,
      HttpSession session) {

    if (!estaAutenticado(session))
      return "redirect:/login";

    ResultadoQuestionario resultado = resultadoService.buscarPorId(id);

    if (resultado == null) {
      return "redirect:/";
    }

    Long usuarioId = obterUsuarioId(session);

    // Se for modo correção: verificar se é o criador do questionário
    if (correcao) {
      if (!resultado.getQuestionario().getCriador().getId().equals(usuarioId)) {
        return "redirect:/meus-questionarios";
      }
      model.addAttribute("modoCorrecao", true);
      model.addAttribute("paginaAtual", "meus-questionarios");
    } else {
      // Modo visualização: verificar se é o próprio respondente
      if (!resultado.getRespondente().getId().equals(usuarioId)) {
        return "redirect:/questionarios-respondidos";
      }
      model.addAttribute("modoCorrecao", false);
      model.addAttribute("paginaAtual", "questionarios-respondidos");
    }

    adicionarDadosUsuario(model, session);
    model.addAttribute("resultado", resultado);

    return "visualizar-resultado";
  }

  @DeleteMapping("/excluir-resultado")
  public ResponseEntity<Map<String, Object>> excluirResultado(@RequestParam Long id, HttpSession session) {
    Map<String, Object> resposta = new HashMap<>();

    if (!estaAutenticado(session)) {
      resposta.put("sucesso", false);
      resposta.put("mensagem", "Usuário não autenticado");
      return ResponseEntity.status(401).body(resposta);
    }

    ResultadoQuestionario resultado = resultadoService.buscarPorId(id);

    if (resultado == null) {
      resposta.put("sucesso", false);
      resposta.put("mensagem", "Resultado não encontrado");
      return ResponseEntity.status(404).body(resposta);
    }

    Long usuarioId = obterUsuarioId(session);

    // Pode excluir se for o próprio respondente OU se for o criador do questionário
    boolean podeExcluir = resultado.getRespondente().getId().equals(usuarioId)
        || resultado.getQuestionario().getCriador().getId().equals(usuarioId);

    if (!podeExcluir) {
      resposta.put("sucesso", false);
      resposta.put("mensagem", "Não autorizado");
      return ResponseEntity.status(403).body(resposta);
    }

    try {
      resultadoService.deletar(id);
      resposta.put("sucesso", true);
      resposta.put("mensagem", "Resultado excluído com sucesso");
      return ResponseEntity.ok(resposta);
    } catch (Exception e) {
      resposta.put("sucesso", false);
      resposta.put("mensagem", "Erro ao excluir resultado: " + e.getMessage());
      return ResponseEntity.status(500).body(resposta);
    }
  }

  @PostMapping("/salvar-correcao")
  public ResponseEntity<Map<String, Object>> salvarCorrecao(
      @RequestBody Map<String, Object> dados,
      HttpSession session) {

    Map<String, Object> resposta = new HashMap<>();

    if (!estaAutenticado(session)) {
      resposta.put("sucesso", false);
      resposta.put("mensagem", "Usuário não autenticado");
      return ResponseEntity.status(401).body(resposta);
    }

    try {
      Long resultadoId = Long.valueOf(dados.get("resultadoId").toString());
      @SuppressWarnings("unchecked")
      Map<String, Object> pontuacoes = (Map<String, Object>) dados.get("pontuacoes");

      ResultadoQuestionario resultado = resultadoService.buscarPorId(resultadoId);

      if (resultado == null) {
        resposta.put("sucesso", false);
        resposta.put("mensagem", "Resultado não encontrado");
        return ResponseEntity.status(404).body(resposta);
      }

      // Verificar se é o criador do questionário
      if (!resultado.getQuestionario().getCriador().getId().equals(obterUsuarioId(session))) {
        resposta.put("sucesso", false);
        resposta.put("mensagem", "Não autorizado");
        return ResponseEntity.status(403).body(resposta);
      }

      // Atualizar pontuações dos itens e recalcular nota final
      resultadoService.atualizarPontuacoes(resultadoId, pontuacoes);

      // Recarregar resultado atualizado para pegar as novas estatísticas
      resultado = resultadoService.buscarPorId(resultadoId);

      resposta.put("sucesso", true);
      resposta.put("mensagem", "Correção salva com sucesso");
      resposta.put("questionarioId", resultado.getQuestionario().getId());
      resposta.put("quantidadeAcertos", resultado.getQuantidadeAcertos());
      resposta.put("quantidadeErros", resultado.getQuantidadeErros());
      return ResponseEntity.ok(resposta);

    } catch (Exception e) {
      resposta.put("sucesso", false);
      resposta.put("mensagem", "Erro ao salvar correção: " + e.getMessage());
      return ResponseEntity.status(500).body(resposta);
    }
  }
}