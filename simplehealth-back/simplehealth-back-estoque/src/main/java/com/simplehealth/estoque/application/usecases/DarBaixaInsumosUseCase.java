package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.service.EstoqueService;
import com.simplehealth.estoque.application.service.ItemService;
import com.simplehealth.estoque.domain.entity.Item;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DarBaixaInsumosUseCase {

  private final EstoqueService estoqueService;
  private final ItemService itemService;

  public void execute(Long itemId, int quantidadeNecessaria, String destinoConsumo) {

    Item item = itemService.buscarPorId(itemId);
    if (item == null) {
      throw new IllegalArgumentException("Item não encontrado no estoque.");
    }

    Date hoje = new Date();
    if (item.getValidade() != null && !item.getValidade().after(hoje)) {
      throw new IllegalArgumentException("Item vencido. Selecione outro lote ou proceda ao descarte.");
    }

    if (item.getQuantidadeTotal() < quantidadeNecessaria) {
      throw new IllegalArgumentException(
          "Estoque insuficiente. Disponível: " + item.getQuantidadeTotal()
      );
    }

    estoqueService.darBaixa(itemId, quantidadeNecessaria);

    registrarMovimentacao(itemId, quantidadeNecessaria, destinoConsumo);

    boolean estoqueCritico = estoqueService.verificarEstoqueCritico(itemId);
    if (estoqueCritico) {
      System.out.println("Alerta: Estoque crítico para o item: " + item.getNome());
    }
  }

  private void registrarMovimentacao(Long itemId, int quantidade, String destino) {
    System.out.printf(
        "Movimentação registrada: ItemID=%d, Quantidade=%d, Destino=%s, Data=%s%n",
        itemId, quantidade, destino, new Date()
    );
  }
}
