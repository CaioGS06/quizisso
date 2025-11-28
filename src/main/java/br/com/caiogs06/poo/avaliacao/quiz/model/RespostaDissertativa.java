package br.com.caiogs06.poo.avaliacao.quiz.model;

public class RespostaDissertativa extends Resposta {
  private String textoResposta;

  public RespostaDissertativa(Long itemId, String textoResposta) {
    super(itemId);
    this.textoResposta = textoResposta;
  }

  public RespostaDissertativa(Long id, Long itemId, Long resultadoId, String textoResposta) {
    super(id, itemId, resultadoId);
    this.textoResposta = textoResposta;
  }

  @Override
  public String getTipo() {
    return "DISSERTATIVA";
  }

  public String getTextoResposta() {
    return textoResposta;
  }
}