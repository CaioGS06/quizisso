package br.com.caiogs06.poo.avaliacao.quiz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.Resposta;
import br.com.caiogs06.poo.avaliacao.quiz.model.ResultadoQuestionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;
import br.com.caiogs06.poo.avaliacao.quiz.repository.ResultadoDAO;

@Service
public class ResultadoService {

  @Autowired
  private ResultadoDAO resultadoDAO;

  @Autowired
  private RespostaService respostaService;

  @Autowired
  private QuestionarioService questionarioService;

  @Autowired
  private UsuarioService usuarioService;

  public List<ResultadoQuestionario> listarPorRespondente(Long respondenteId) {
    List<ResultadoQuestionario> resultados = resultadoDAO.listarPorRespondente(respondenteId);
    resultados.forEach(this::carregarDadosCompletos);
    return resultados;
  }

  public List<ResultadoQuestionario> listarPorQuestionario(Long questionarioId) {
    List<ResultadoQuestionario> resultados = resultadoDAO.listarPorQuestionario(questionarioId);
    resultados.forEach(this::carregarDadosCompletos);
    return resultados;
  }

  public ResultadoQuestionario buscarResultado(Questionario questionario, Usuario respondente) {
    ResultadoQuestionario resultado = resultadoDAO.buscarPorQuestionarioERespondente(questionario.getId(),
        respondente.getId());
    if (resultado != null) {
      carregarDadosCompletos(resultado);
    }
    return resultado;
  }

  public ResultadoQuestionario buscarPorId(Long id) {
    ResultadoQuestionario resultado = resultadoDAO.buscarPorId(id);
    if (resultado != null) {
      carregarDadosCompletos(resultado);
    }
    return resultado;
  }

  private void carregarDadosCompletos(ResultadoQuestionario resultado) {
    // Carregar Questionario completo
    Questionario questionario = questionarioService.buscarPorId(resultado.getQuestionarioId());
    resultado.setQuestionario(questionario);

    // Carregar Usuario completo
    Usuario respondente = usuarioService.buscarPorId(resultado.getRespondenteId());
    resultado.setRespondente(respondente);

    // Carregar Respostas
    List<Resposta> respostas = respostaService.listarPorResultado(resultado.getId());
    resultado.setRespostas(respostas);
  }

  /**
   * Processa a submissão completa de um questionário.
   * Calcula nota, salva resultado e salva todas as respostas.
   */
  @Transactional
  public Long submeterRespostas(ResultadoQuestionario resultado) {
    // Validação de negócio
    int totalItens = resultado.getQuestionario().getListaItens().size();
    int totalRespondido = resultado.getRespostas().size();

    if (totalRespondido != totalItens) {
      throw new IllegalStateException("Questionário incompleto. Respondidas: " + totalRespondido + "/" + totalItens);
    }

    // Calcula nota
    resultado.calcularNotaFinal();

    // Salva Resultado (Pai)
    Long resultadoId = resultadoDAO.salvar(resultado);
    resultado.setId(resultadoId);

    // Salva Respostas (Filhos)
    for (Resposta resposta : resultado.getRespostas()) {
      respostaService.salvar(resposta, resultadoId);
    }

    return resultadoId;
  }

  public boolean existemResultadosPorQuestionario(Long questionarioId) {
    return resultadoDAO.existemResultadosPorQuestionario(questionarioId);
  }

  @Transactional
  public void deletarPorQuestionario(Long questionarioId) {
    // Primeiro buscar todos os IDs de resultados para deletar as respostas
    List<Long> resultadoIds = resultadoDAO.listarIdsPorQuestionario(questionarioId);

    // Deletar todas as respostas de cada resultado
    for (Long resultadoId : resultadoIds) {
      respostaService.deletarPorResultado(resultadoId);
    }

    // Depois deletar os resultados
    resultadoDAO.deletarPorQuestionario(questionarioId);
  }

  @Transactional
  public void deletar(Long id) {
    // Deletar todas as respostas associadas ao resultado
    respostaService.deletarPorResultado(id);

    // Deletar o resultado
    resultadoDAO.deletar(id);
  }

  @Transactional
  public void atualizarPontuacoes(Long resultadoId, Map<String, Object> pontuacoes) {
    ResultadoQuestionario resultado = buscarPorId(resultadoId);
    if (resultado == null) {
      throw new IllegalArgumentException("Resultado não encontrado");
    }

    // Atualizar pontuações de cada resposta
    for (Map.Entry<String, Object> entry : pontuacoes.entrySet()) {
      Long itemId = Long.valueOf(entry.getKey());
      Double novaPontuacao = Double.valueOf(entry.getValue().toString());

      // Buscar a resposta correspondente ao item
      Resposta resposta = resultado.buscarRespostaPorItemId(itemId);
      if (resposta != null) {
        respostaService.atualizarPontuacaoItem(resposta.getId(), novaPontuacao);
      }
    }

    // Recarregar resultado com pontuações atualizadas
    resultado = buscarPorId(resultadoId);

    // Recalcular nota final
    resultado.calcularNotaFinal();

    // Atualizar nota final no banco
    resultadoDAO.atualizarNotaFinal(resultadoId, resultado.getNotaFinal());
  }
}