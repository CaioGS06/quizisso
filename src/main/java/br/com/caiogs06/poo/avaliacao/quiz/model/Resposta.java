package br.com.caiogs06.poo.avaliacao.quiz.model;

public abstract class Resposta {
  private Long id;
  private final Long itemId;
  private Long resultadoId;
  private final Double pontuacaoObtida;

  public Resposta(Long itemId, Double pontuacaoObtida) {
    this.itemId = itemId;
    this.pontuacaoObtida = pontuacaoObtida;
  }

  public Resposta(Long id, Long itemId, Long resultadoId, Double pontuacaoObtida) {
    this.id = id;
    this.itemId = itemId;
    this.resultadoId = resultadoId;
    this.pontuacaoObtida = pontuacaoObtida;
  }

  public abstract String getTipo();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getItemId() {
    return itemId;
  }

  public Long getResultadoId() {
    return resultadoId;
  }

  public void setResultadoId(Long resultadoId) {
    this.resultadoId = resultadoId;
  }

  public Double getPontuacaoObtida() {
    return pontuacaoObtida;
  }

  // public void setPontuacaoObtida(Double pontuacaoObtida) {
  // this.pontuacaoObtida = pontuacaoObtida;
  // }
}