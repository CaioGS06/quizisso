package br.com.caiogs06.poo.avaliacao.quiz.model;

public class Alternativa {
  private final Long id;
  private final String descricao;
  private final boolean estaCorreta;
  private final int ordem;

  public Alternativa(Long id, String descricao, boolean correta, int ordem) {
    this.id = id;
    this.descricao = descricao;
    this.estaCorreta = correta;
    this.ordem = ordem;
  }

  public Long getId() {
    return id;
  }

  public String getDescricao() {
    return descricao;
  }

  public boolean getEstaCorreta() {
    return estaCorreta;
  }

  public int getOrdem() {
    return ordem;
  }
}