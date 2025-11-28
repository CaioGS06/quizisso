package br.com.caiogs06.poo.avaliacao.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class QuestaoAlternativa extends Item<RespostaAlternativa> {
  private List<Alternativa> alternativas = new ArrayList<>();

  public QuestaoAlternativa(Long id, String enunciado, double pontuacaoMaxima, int ordem) {
    super(id, enunciado, pontuacaoMaxima, ordem);
  }

  @Override
  public boolean respostaEstaCorreta(RespostaAlternativa resposta) {
    if (resposta == null || resposta.getAlternativaId() == null)
      return false;

    return alternativas.stream()
        .filter(a -> a.getId().equals(resposta.getAlternativaId()))
        .findFirst()
        .map(Alternativa::getEstaCorreta)
        .orElse(false);
  }

  @Override
  public double calcularPontuacao(RespostaAlternativa resposta) {
    return respostaEstaCorreta(resposta) ? getPontuacaoMaxima() : 0.0;
  }

  @Override
  public String getTipo() {
    return "ALTERNATIVA";
  }

  public List<Alternativa> getAlternativas() {
    return alternativas;
  }

  public void setAlternativas(List<Alternativa> alternativas) {
    this.alternativas = (alternativas != null) ? alternativas : new ArrayList<>();
  }
}