package br.com.caiogs06.poo.avaliacao.quiz.model;

public class QuestaoDissertativa extends Item<RespostaDissertativa> {
  private String gabarito;

  public QuestaoDissertativa(Long id, String enunciado, String gabarito, double pontuacaoMaxima, int ordem) {
    super(id, enunciado, pontuacaoMaxima, ordem);
    this.gabarito = gabarito;
  }

  @Override
  public boolean respostaEstaCorreta(RespostaDissertativa resposta) {
    if (resposta == null || resposta.getTextoResposta() == null)
      return false;
    return resposta.getTextoResposta().trim().equalsIgnoreCase(gabarito.trim());
  }

  @Override
  public double calcularPontuacao(RespostaDissertativa resposta) {
    return respostaEstaCorreta(resposta) ? getPontuacaoMaxima() : 0.0;
  }

  @Override
  public String getTipo() {
    return "DISSERTATIVA";
  }

  public String getGabarito() {
    return gabarito;
  }

  public void setGabarito(String gabarito) {
    this.gabarito = gabarito;
  }
}