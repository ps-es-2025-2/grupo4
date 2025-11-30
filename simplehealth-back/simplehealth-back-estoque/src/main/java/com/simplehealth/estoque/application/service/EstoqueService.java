package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Estoque;
import com.simplehealth.estoque.domain.entity.Item;
import com.simplehealth.estoque.infrastructure.repositories.EstoqueRepository;
import com.simplehealth.estoque.infrastructure.repositories.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

  private final EstoqueRepository estoqueRepository;
  private final ItemRepository itemRepository;

  public void darBaixa(Long itemId, int quantidade) {
    Item item = itemRepository.findById(itemId);
    if (item == null) {
      throw new IllegalArgumentException("Item não encontrado no estoque.");
    }
    int qtdeAtual = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    if (qtdeAtual < quantidade) {
      throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + qtdeAtual);
    }
    item.setQuantidadeTotal(qtdeAtual - quantidade);
    itemRepository.save(item);
  }

  public boolean verificarEstoqueCritico(Long itemId) {
    Item item = itemRepository.findById(itemId);
    if (item == null) {
      return false;
    }
    return item.getQuantidadeTotal() <= 5;
  }

  public void solicitarReposicao(Long itemId, int quantidade) {
    Item item = itemRepository.findById(itemId);
    if (item == null) {
      return;
    }
    int qtdeAtual = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    item.setQuantidadeTotal(qtdeAtual + quantidade);
    itemRepository.save(item);
  }

  public void somarQuantidade(Long itemId, int quantidade) {
    Item item = itemRepository.findById(itemId);
    if (item == null) {
      throw new IllegalArgumentException("Item não encontrado no estoque.");
    }
    int qtdeAtual = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    item.setQuantidadeTotal(qtdeAtual + quantidade);
    itemRepository.save(item);
  }

  public Estoque buscarEstoquePrincipal() {
    List<Estoque> estoques = estoqueRepository.findAll();
    if (estoques.isEmpty()) {
      Estoque novo = new Estoque();
      novo.setLocal("Estoque Principal");
      return estoqueRepository.save(novo);
    }
    return estoques.get(0);
  }
}
