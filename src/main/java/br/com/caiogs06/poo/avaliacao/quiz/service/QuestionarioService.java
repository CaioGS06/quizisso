package br.com.caiogs06.poo.avaliacao.quiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caiogs06.poo.avaliacao.quiz.model.Item;
import br.com.caiogs06.poo.avaliacao.quiz.model.Questionario;
import br.com.caiogs06.poo.avaliacao.quiz.model.Usuario;
import br.com.caiogs06.poo.avaliacao.quiz.repository.QuestionarioDAO;
import br.com.caiogs06.poo.avaliacao.quiz.repository.UsuarioDAO;

@Service
public class QuestionarioService {

  @Autowired
  private QuestionarioDAO questionarioDAO;

  @Autowired
  private UsuarioDAO usuarioDAO;

  @Autowired
  private ItemService itemService;

  public List<Questionario> listarTodos() {
    List<Questionario> questionarios = questionarioDAO.listarTodos();
    questionarios.forEach(this::carregarDadosCompletos);
    return questionarios;
  }

  public List<Questionario> listarPorCriador(Long criadorId) {
    List<Questionario> questionarios = questionarioDAO.listarPorCriador(criadorId);
    questionarios.forEach(this::carregarDadosCompletos);
    return questionarios;
  }

  public Questionario buscarPorId(Long id) {
    Questionario q = questionarioDAO.buscarPorId(id);
    if (q != null) {
      carregarDadosCompletos(q);
    }
    return q;
  }

  private void carregarDadosCompletos(Questionario q) {
    // Carregar Usuario completo
    Usuario criador = usuarioDAO.buscarPorId(q.getCriadorId());
    q.setCriador(criador);

    // Carregar itens
    List<Item<?>> itens = itemService.listarPorQuestionario(q.getId());
    q.setListaItens(itens);
  }

  @Transactional
  public void salvar(Questionario questionario) {
    if (!questionario.isValido()) {
      throw new IllegalStateException("Questionário inválido (Verifique título e itens)");
    }
    questionarioDAO.salvar(questionario);

  }

  @Transactional
  public void atualizarMetadados(Questionario questionario, Usuario editor) {
    validarPermissaoEdicao(questionario, editor);
    questionarioDAO.atualizar(questionario);
  }

  @Transactional
  public void apagarQuestionario(Long id, Usuario usuario) {
    Questionario q = buscarPorId(id);
    if (q == null)
      throw new IllegalArgumentException("Questionário não encontrado");

    validarPermissaoEdicao(q, usuario);
    questionarioDAO.deletar(id);
  }

  // --- Gestão de Itens (Orquestração) ---

  @Transactional
  public Long adicionarItem(Long questionarioId, Item<?> item, Usuario editor) {
    Questionario q = buscarPorId(questionarioId);
    validarPermissaoEdicao(q, editor);

    q.adicionarItem(item); // Adiciona na memória para validação
    return itemService.salvar(item, questionarioId);
  }

  @Transactional
  public void removerItem(Long questionarioId, Long itemId, Usuario editor) {
    Questionario q = buscarPorId(questionarioId);
    validarPermissaoEdicao(q, editor);

    itemService.deletar(itemId);
  }

  @Transactional
  public void editarItem(Long questionarioId, Item<?> itemAtualizado, Usuario editor) {
    Questionario q = buscarPorId(questionarioId);
    validarPermissaoEdicao(q, editor);

    itemService.atualizar(itemAtualizado);
  }

  private void validarPermissaoEdicao(Questionario q, Usuario usuario) {
    if (q == null || !q.podeSerEditadoPor(usuario)) {
      throw new IllegalStateException("Operação não permitida: Apenas o criador pode editar.");
    }
  }
}