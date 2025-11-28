package br.com.caiogs06.poo.avaliacao.quiz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Questionario {
  private Long id;
  private String titulo;
  private String descricao;
  private Usuario criador;
  private int bannerId;
  private LocalDateTime dataCriacao;
  private List<Item<?>> listaItens = new ArrayList<>();

  public Questionario() {
  }

  public Questionario(Long id, String titulo, String descricao, Long criadorId, Integer bannerId,
      LocalDateTime dataCriacao) {
    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.criador = new Usuario(criadorId);
    this.bannerId = bannerId != null ? bannerId : 0;
    this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
  }

  // Domain Logic: Validações
  public boolean isValido() {
    return titulo != null && !titulo.trim().isEmpty() && criador != null && !listaItens.isEmpty();
  }

  public boolean podeSerEditadoPor(Usuario usuario) {
    return usuario != null && this.criador != null && usuario.getId().equals(this.criador.getId());
  }

  public double obterPontuacaoMaxima() {
    return listaItens.stream().mapToDouble(Item::getPontuacaoMaxima).sum();
  }

  // Gestão de Itens
  public void adicionarItem(Item<?> item) {
    if (item == null)
      throw new IllegalArgumentException("Item nulo");
    item.setOrdem(listaItens.size() + 1);
    listaItens.add(item);
  }

  public List<Item<?>> getListaItens() {
    return Collections.unmodifiableList(listaItens);
  }

  // Setter usado pelo DAO para preencher a lista após carregar do banco
  public void setListaItens(List<Item<?>> listaItens) {
    this.listaItens = listaItens != null ? listaItens : new ArrayList<>();
  }

  // Getters e Setters básicos
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public Usuario getCriador() {
    return criador;
  }

  public void setCriador(Usuario criador) {
    this.criador = criador;
  }

  public Long getCriadorId() {
    return criador != null ? criador.getId() : null;
  }

  public Integer getBannerId() {
    return bannerId;
  }

  public LocalDateTime getDataCriacao() {
    return dataCriacao;
  }
}