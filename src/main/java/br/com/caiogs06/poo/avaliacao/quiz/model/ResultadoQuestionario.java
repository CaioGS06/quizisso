package br.com.caiogs06.poo.avaliacao.quiz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResultadoQuestionario {
  private Long id;
  private Questionario questionario;
  private Usuario respondente;
  private Double notaFinal;
  private LocalDateTime dataSubmissao;
  private List<Resposta> respostas = new ArrayList<>();

  public ResultadoQuestionario(Questionario questionario, Usuario respondente) {
    this.questionario = questionario;
    this.respondente = respondente;
    this.dataSubmissao = LocalDateTime.now();
  }

  // Construtor completo para o DAO
  public ResultadoQuestionario(Long id, Questionario questionario, Usuario respondente, Double notaFinal,
      LocalDateTime dataSubmissao) {
    this.id = id;
    this.questionario = questionario;
    this.respondente = respondente;
    this.notaFinal = notaFinal;
    this.dataSubmissao = dataSubmissao;
  }

  // Construtor usado pelo DAO (sem carregar objetos relacionados)
  public ResultadoQuestionario(Long id, Long questionarioId, Long respondenteId, Double notaFinal,
      LocalDateTime dataSubmissao) {
    this.id = id;
    this.questionario = new Questionario();
    this.questionario.setId(questionarioId);
    this.respondente = new Usuario(respondenteId);
    this.notaFinal = notaFinal;
    this.dataSubmissao = dataSubmissao;
  }

  public void adicionarResposta(Resposta resposta) {
    respostas.add(resposta);
  }

  public void calcularNotaFinal() {
    if (questionario == null)
      return;

    // Polimorfismo: o item sabe calcular a nota baseado na resposta
    double total = 0.0;
    for (Resposta resp : respostas) {
      Item item = buscarItemPorId(resp.getItemId());
      if (item != null) {
        // Warning suprimido: sabemos que o tipo da resposta bate com o item pela lógica
        // de negócio
        total += item.calcularPontuacao(resp);
      }
    }
    this.notaFinal = total;
  }

  private Item buscarItemPorId(Long itemId) {
    return questionario.getListaItens().stream()
        .filter(i -> i.getId().equals(itemId))
        .findFirst()
        .orElse(null);
  }

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Questionario getQuestionario() {
    return questionario;
  }

  public void setQuestionario(Questionario questionario) {
    this.questionario = questionario;
  }

  public Long getQuestionarioId() {
    return questionario != null ? questionario.getId() : null;
  }

  public Usuario getRespondente() {
    return respondente;
  }

  public void setRespondente(Usuario respondente) {
    this.respondente = respondente;
  }

  public Long getRespondenteId() {
    return respondente != null ? respondente.getId() : null;
  }

  public Double getNotaFinal() {
    return notaFinal;
  }

  public LocalDateTime getDataSubmissao() {
    return dataSubmissao;
  }

  public List<Resposta> getRespostas() {
    return respostas;
  }

  public void setRespostas(List<Resposta> respostas) {
    this.respostas = respostas;
  }
}