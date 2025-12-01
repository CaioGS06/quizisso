package br.com.caiogs06.poo.avaliacao.quiz.model;

import java.util.Arrays;

public enum Imagens {
  FOTO_CAMARAO(1, "usuario1.jpg", "Foto Camarão", TipoImagem.USUARIO),
  FOTO_PEIXE(2, "usuario2.jpg", "Foto Peixe", TipoImagem.USUARIO),
  FOTO_CORVO(3, "usuario3.jpg", "Foto Corvo", TipoImagem.USUARIO),
  FOTO_SAPO(4, "usuario4.jpg", "Foto Sapo", TipoImagem.USUARIO),
  BANNER_PADRAO(0, "banner0.jpg", "Banner Padrão", TipoImagem.BANNER),
  BANNER_JAVA(1, "banner1.jpg", "Banner Java", TipoImagem.BANNER),
  BANNER_POO(2, "banner2.jpg", "Banner POO", TipoImagem.BANNER),
  BANNER_ESTRUTURAS_DADOS(3, "banner3.jpg", "Banner Estruturas de Dados", TipoImagem.BANNER),
  BANNER_BANCO_DADOS(4, "banner4.jpg", "Banner Banco de Dados", TipoImagem.BANNER),
  BANNER_HTML_CSS(5, "banner5.jpg", "Banner HTML e CSS", TipoImagem.BANNER),
  BANNER_JAVASCRIPT(6, "banner6.jpg", "Banner JavaScript", TipoImagem.BANNER),
  BANNER_GIT(7, "banner7.jpg", "Banner Git", TipoImagem.BANNER),
  BANNER_PYTHON(8, "banner8.jpg", "Banner Python", TipoImagem.BANNER),
  BANNER_REDES(9, "banner9.jpg", "Banner Redes de Computadores", TipoImagem.BANNER),
  BANNER_SPRING(10, "banner10.jpg", "Banner Spring Framework", TipoImagem.BANNER);

  private enum TipoImagem {
    USUARIO,
    BANNER
  }

  private final int id;
  private final String nomeArquivo;
  private final String descricao;
  private final TipoImagem tipo;

  Imagens(int id, String nomeArquivo, String descricao, TipoImagem tipo) {
    this.id = id;
    this.nomeArquivo = nomeArquivo;
    this.descricao = descricao;
    this.tipo = tipo;
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

  public TipoImagem getTipo() {
    return tipo;
  }

  public static Imagens porId(Integer id) {
    if (id == null) {
      return null;
    }

    for (Imagens foto : values()) {
      if (foto.getId() == id) {
        return foto;
      }
    }

    return null; // Valor padrão se não encontrado
  }

  public static Imagens[] obterFotosUsuarios() {
    return Arrays.stream(values())
        .filter(imagem -> imagem.getTipo() == TipoImagem.USUARIO)
        .toArray(Imagens[]::new);
  }

  public static Imagens[] obterBanners() {
    return Arrays.stream(values())
        .filter(imagem -> imagem.getTipo() == TipoImagem.BANNER)
        .toArray(Imagens[]::new);
  }
}