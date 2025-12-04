package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Estoque;
import com.simplehealth.estoque.domain.entity.Item;
import com.simplehealth.estoque.infrastructure.repositories.EstoqueRepository;
import com.simplehealth.estoque.infrastructure.repositories.ItemRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

  private final EstoqueRepository estoqueRepository;
  private final ItemRepository itemRepository;

  public Estoque salvar(Estoque estoque) {
    if (estoque.getIdEstoque() == null) {
      estoque.setIdEstoque(UUID.randomUUID());
    }
    return estoqueRepository.save(estoque);
  }

  public Estoque buscarPorId(UUID id) {
    return estoqueRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado."));
  }

  public List<Estoque> listarTodos() {
    return estoqueRepository.findAll();
  }

  public void deletar(UUID id) {
    estoqueRepository.deleteById(id);
  }

  public void darBaixa(UUID itemId, int quantidade) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("Item não encontrado no estoque."));
    int qtdeAtual = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    if (qtdeAtual < quantidade) {
      throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + qtdeAtual);
    }
    item.setQuantidadeTotal(qtdeAtual - quantidade);
    itemRepository.save(item);
  }

  public boolean verificarEstoqueCritico(UUID itemId) {
    return itemRepository.findById(itemId)
        .map(i -> i.getQuantidadeTotal() != null && i.getQuantidadeTotal() <= 5)
        .orElse(false);
  }

  public void solicitarReposicao(UUID itemId, int quantidade) {
    itemRepository.findById(itemId).ifPresent(i -> {
      int qt = i.getQuantidadeTotal() != null ? i.getQuantidadeTotal() : 0;
      i.setQuantidadeTotal(qt + quantidade);
      itemRepository.save(i);
    });
  }

  public void somarQuantidade(UUID itemId, int quantidade) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("Item não encontrado no estoque."));
    int qt = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    item.setQuantidadeTotal(qt + quantidade);
    itemRepository.save(item);
  }

  public Estoque buscarEstoquePrincipal() {
    List<Estoque> list = estoqueRepository.findAll();
    if (list.isEmpty()) {
      Estoque novo = new Estoque();
      novo.setLocal("Estoque Principal");
      return estoqueRepository.save(novo);
    }
    return list.get(0);
  }

}
