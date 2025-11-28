package br.com.caiogs06.poo.avaliacao.quiz.model;

import java.time.LocalDateTime;

public class Usuario {
  private Long id;
  private String nome;
  private String email;
  private String senha;
  private int fotoId;
  private LocalDateTime dataCriacao;

  public Usuario(Long id) {
    this.id = id;
  }

  public Usuario(Long id, String nome, String email, String senha, Integer fotoId, LocalDateTime dataCriacao) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.fotoId = fotoId != null ? fotoId : 0;
    this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
  }

  // Lógica de domínio: Validação de mudança de senha
  public void alterarSenha(String senhaAntiga, String senhaNova) {
    if (!this.senha.equals(senhaAntiga)) {
      throw new IllegalArgumentException("Senha antiga incorreta");
    }
    if (senhaNova == null || senhaNova.trim().isEmpty()) {
      throw new IllegalArgumentException("Nova senha não pode ser vazia");
    }
    this.senha = senhaNova;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }

  public Integer getFotoId() {
    return fotoId;
  }

  public LocalDateTime getDataCriacao() {
    return dataCriacao;
  }
}