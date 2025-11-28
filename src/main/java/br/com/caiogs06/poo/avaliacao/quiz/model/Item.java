package br.com.caiogs06.poo.avaliacao.quiz.model;

public abstract class Item<T extends Resposta> {
  private Long id;
  private String enunciado;
  private double pontuacaoMaxima;
  private int ordem;

  public Item(Long id, String enunciado, double pontuacaoMaxima, int ordem) {
    this.id = id;
    this.enunciado = enunciado;
    this.pontuacaoMaxima = pontuacaoMaxima;
    this.ordem = ordem;
  }

  public Item(String enunciado, double pontuacaoMaxima) {
    this(null, enunciado, pontuacaoMaxima, 0);
  }

  // Métodos Abstratos (Polimorfismo)
  public abstract boolean respostaEstaCorreta(T resposta);

  public abstract double calcularPontuacao(T resposta);

  public abstract String getTipo(); // Usado pelo DAO para saber se é "DISSERTATIVA" ou "ALTERNATIVA"

  // Getters
  public Long getId() {
    return id;
  }

  public String getEnunciado() {
    return enunciado;
  }

  public double getPontuacaoMaxima() {
    return pontuacaoMaxima;
  }

  public int getOrdem() {
    return ordem;
  }

  public void setOrdem(int ordem) {
    this.ordem = ordem;
  }
}