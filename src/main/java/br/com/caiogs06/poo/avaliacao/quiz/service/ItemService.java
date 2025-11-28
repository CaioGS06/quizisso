package br.com.caiogs06.poo.avaliacao.quiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caiogs06.poo.avaliacao.quiz.model.Alternativa;
import br.com.caiogs06.poo.avaliacao.quiz.model.Item;
import br.com.caiogs06.poo.avaliacao.quiz.model.QuestaoAlternativa;
import br.com.caiogs06.poo.avaliacao.quiz.repository.AlternativaDAO;
import br.com.caiogs06.poo.avaliacao.quiz.repository.ItemDAO;

@Service
public class ItemService {

  @Autowired
  private ItemDAO itemDAO;

  @Autowired
  private AlternativaDAO alternativaDAO;

  public List<Item<?>> listarPorQuestionario(Long questionarioId) {
    List<Item<?>> itens = itemDAO.listarPorQuestionario(questionarioId);
    itens.forEach(this::carregarAlternativas);
    return itens;
  }

  public Item<?> buscarPorId(Long id) {
    Item<?> item = itemDAO.buscarPorId(id);
    if (item != null) {
      carregarAlternativas(item);
    }
    return item;
  }

  @Transactional
  public Long salvar(Item<?> item, Long questionarioId) {
    Long idGerado = itemDAO.salvar(item, questionarioId);

    // Se for questão alternativa, salvar as alternativas
    if (item instanceof QuestaoAlternativa qa) {
      for (Alternativa alt : qa.getAlternativas()) {
        alternativaDAO.salvar(alt, idGerado);
      }
    }

    return idGerado;
  }

  @Transactional
  public void atualizar(Item<?> item) {
    itemDAO.atualizar(item);

    // Se for questão alternativa, atualizar as alternativas
    if (item instanceof QuestaoAlternativa qa) {
      // Estratégia simples: remove todas e recria
      alternativaDAO.deletarPorItem(qa.getId());
      for (Alternativa alt : qa.getAlternativas()) {
        alternativaDAO.salvar(alt, qa.getId());
      }
    }
  }

  @Transactional
  public void deletar(Long id) {
    itemDAO.deletar(id);
  }

  private void carregarAlternativas(Item<?> item) {
    if (item instanceof QuestaoAlternativa qa) {
      List<Alternativa> alts = alternativaDAO.listarPorItem(qa.getId());
      qa.setAlternativas(alts);
    }
  }
}