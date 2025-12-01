package br.com.caiogs06.poo.avaliacao.quiz.model;

public class RespostaAlternativa extends Resposta {
  private final Long alternativaId;

  public RespostaAlternativa(Long itemId, Long alternativaId, Double pontuacaoObtida) {
    super(itemId, pontuacaoObtida);
    this.alternativaId = alternativaId;
  }

  public RespostaAlternativa(Long id, Long itemId, Long resultadoId, Long alternativaId, Double pontuacaoObtida) {
    super(id, itemId, resultadoId, pontuacaoObtida);
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