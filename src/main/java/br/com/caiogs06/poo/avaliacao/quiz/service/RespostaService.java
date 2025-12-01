package br.com.caiogs06.poo.avaliacao.quiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caiogs06.poo.avaliacao.quiz.model.Resposta;
import br.com.caiogs06.poo.avaliacao.quiz.repository.RespostaDAO;

@Service
public class RespostaService {

  @Autowired
  private RespostaDAO respostaDAO;

  public List<Resposta> listarPorResultado(Long resultadoId) {
    return respostaDAO.listarPorResultado(resultadoId);
  }

  public Resposta buscarPorId(Long id) {
    return respostaDAO.buscarPorId(id);
  }

  @Transactional
  public Long salvar(Resposta resposta, Long resultadoId) {
    if (resposta == null)
      throw new IllegalArgumentException("Resposta nula");
    return respostaDAO.salvar(resposta, resultadoId);
  }

  @Transactional
  public void atualizar(Resposta resposta) {
    if (resposta == null)
      throw new IllegalArgumentException("Resposta nula");
    respostaDAO.atualizar(resposta);
  }

  @Transactional
  public void atualizarPontuacaoItem(Long respostaId, Double novaPontuacao) {
    respostaDAO.atualizarPontuacao(respostaId, novaPontuacao);
  }

  @Transactional
  public void deletar(Long id) {
    respostaDAO.deletar(id);
  }

  @Transactional
  public void deletarPorResultado(Long resultadoId) {
    respostaDAO.deletarPorResultado(resultadoId);
  }
}