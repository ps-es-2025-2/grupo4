package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.service.ItemService;
import com.simplehealth.estoque.domain.entity.Item;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControlarValidadeUseCase {

  private final ItemService itemService;

  public List<Item> execute(int diasAntecedencia, boolean incluirVencidos, boolean descartarItens, String codigoCusto) {
    List<Item> itensParaControle = buscarItensPorValidade(diasAntecedencia, incluirVencidos);

    if (descartarItens && !itensParaControle.isEmpty()) {
      descartarItens(itensParaControle, codigoCusto);
    }

    return itensParaControle;
  }

  private List<Item> buscarItensPorValidade(int diasAntecedencia, boolean incluirVencidos) {
    List<Item> todosItens = itemService.listarTodos();
    Date hoje = new Date();
    List<Item> resultado = new ArrayList<>();

    for (Item item : todosItens) {
      if (item.getValidade() == null) {
        continue;
      }
      long diff = item.getValidade().getTime() - hoje.getTime();
      long dias = diff / (1000 * 60 * 60 * 24);

      if ((incluirVencidos && dias < 0) || (dias >= 0 && dias <= diasAntecedencia)) {
        resultado.add(item);
      }
    }

    if (resultado.isEmpty()) {
      System.out.println("Nenhum item encontrado no critério de validade.");
    }

    return resultado;
  }

  private void descartarItens(List<Item> itens, String codigoCusto) {
    Date dataAtual = new Date();

    for (Item item : itens) {
      int qtde = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
      if (qtde <= 0) {
        continue;
      }

      item.setQuantidadeTotal(0);
      itemService.salvar(item);

      registrarMovimentacaoDescarte(item, qtde, codigoCusto, dataAtual);
    }
  }

  private void registrarMovimentacaoDescarte(Item item, int quantidade, String codigoCusto, Date data) {
    System.out.printf(
        "Descarte registrado: ItemID=%d, Quantidade=%d, CódigoCusto=%s, Data=%s%n",
        item.getIdItem(), quantidade, codigoCusto, data
    );
  }
}
