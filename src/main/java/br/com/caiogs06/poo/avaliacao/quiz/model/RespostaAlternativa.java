package br.com.caiogs06.poo.avaliacao.quiz.model;

public class RespostaAlternativa extends Resposta {
  private Long alternativaId;

  public RespostaAlternativa(Long itemId, Long alternativaId) {
    super(itemId);
    this.alternativaId = alternativaId;
  }

  public RespostaAlternativa(Long id, Long itemId, Long resultadoId, Long alternativaId) {
    super(id, itemId, resultadoId);
    this.alternativaId = alternativaId;
  }

  @Override
  public String getTipo() {
    return "ALTERNATIVA";
  }

  public Long getAlternativaId() {
    return alternativaId;
  }
}