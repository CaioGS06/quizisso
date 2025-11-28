package br.com.caiogs06.poo.avaliacao.quiz.model;

public enum FotoPerfil {
  USUARIO1(1, "usuario1.jpg", "Camarão"),
  USUARIO2(2, "usuario2.jpg", "Peixe"),
  USUARIO3(3, "usuario3.jpg", "Corvo"),
  USUARIO4(4, "usuario4.jpg", "Sapo");

  private final int id;
  private final String nomeArquivo;
  private final String descricao;

  FotoPerfil(int id, String nomeArquivo, String descricao) {
    this.id = id;
    this.nomeArquivo = nomeArquivo;
    this.descricao = descricao;
  }

  public int getId() {
    return id;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public String getDescricao() {
    return descricao;
  }

  public static FotoPerfil porId(Integer id) {
    if (id == null) {
      return USUARIO1; // Valor padrão
    }
    for (FotoPerfil foto : values()) {
      if (foto.getId() == id) {
        return foto;
      }
    }
    return USUARIO1; // Valor padrão se não encontrado
  }
}