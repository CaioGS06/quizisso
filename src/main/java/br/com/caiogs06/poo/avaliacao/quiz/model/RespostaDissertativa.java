package br.com.caiogs06.poo.avaliacao.quiz.model;

public class RespostaDissertativa extends Resposta {
  private final String textoResposta;

  public RespostaDissertativa(Long itemId, String textoResposta, Double pontuacaoObtida) {
    super(itemId, pontuacaoObtida);
    this.textoResposta = textoResposta;
  }

  public RespostaDissertativa(Long id, Long itemId, Long resultadoId, String textoResposta, Double pontuacaoObtida) {
    super(id, itemId, resultadoId, pontuacaoObtida);
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