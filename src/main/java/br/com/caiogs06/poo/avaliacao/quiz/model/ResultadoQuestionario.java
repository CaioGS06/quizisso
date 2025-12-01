package br.com.caiogs06.poo.avaliacao.quiz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResultadoQuestionario {
  private Long id;
  private Questionario questionario;
  private Usuario respondente;
  private Double notaFinal;
  private final LocalDateTime dataSubmissao;
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

  public int getQuantidadeQuestoes() {
    return questionario.getListaItens().size();
  }

  public int getQuantidadeAcertos() {
    if (questionario == null)
      return 0;

    int corretas = 0;
    for (Resposta resp : respostas) {
      Item<? extends Resposta> item = buscarItemPorId(resp.getItemId());
      if (item != null && item instanceof QuestaoAlternativa && resp instanceof RespostaAlternativa &&
          ((QuestaoAlternativa) item).respostaEstaCorreta((RespostaAlternativa) resp)) {
        corretas++;
      } else if (item != null && item instanceof QuestaoDissertativa && resp instanceof RespostaDissertativa &&
          ((QuestaoDissertativa) item).respostaEstaCorreta((RespostaDissertativa) resp)) {
        corretas++;
      }
    }
    return corretas;
  }

  public int getQuantidadeErros() {
    if (questionario == null)
      return 0;
    return questionario.getListaItens().size() - getQuantidadeAcertos();
  }

  public void calcularNotaFinal() {
    if (questionario == null)
      return;

    // Soma as pontuações obtidas de cada resposta
    double total = 0.0;
    for (Resposta resp : respostas) {
      if (resp.getPontuacaoObtida() != null) {
        total += resp.getPontuacaoObtida();
      }
    }
    this.notaFinal = total;
  }

  private Item<? extends Resposta> buscarItemPorId(Long itemId) {
    return questionario.getListaItens().stream()
        .filter(i -> i.getId().equals(itemId))
        .findFirst()
        .orElse(null);
  }

  public Resposta buscarRespostaPorItemId(Long itemId) {
    return respostas.stream()
        .filter(r -> r.getItemId().equals(itemId))
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