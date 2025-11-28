package br.com.caiogs06.poo.avaliacao.quiz.model;

public abstract class Resposta {
  private Long id;
  private Long itemId;
  private Long resultadoId;

  public Resposta(Long itemId) {
    this.itemId = itemId;
  }

  public Resposta(Long id, Long itemId, Long resultadoId) {
    this.id = id;
    this.itemId = itemId;
    this.resultadoId = resultadoId;
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
}