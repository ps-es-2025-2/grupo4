package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.dto.ItemEntradaDTO;
import com.simplehealth.estoque.application.service.EstoqueService;
import com.simplehealth.estoque.application.service.FornecedorService;
import com.simplehealth.estoque.application.service.ItemService;
import com.simplehealth.estoque.application.service.PedidoService;
import com.simplehealth.estoque.domain.entity.Fornecedor;
import com.simplehealth.estoque.domain.entity.Item;
import com.simplehealth.estoque.domain.entity.Pedido;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntradaItensUseCase {

  private final EstoqueService estoqueService;
  private final ItemService itemService;
  private final FornecedorService fornecedorService;
  private final PedidoService pedidoService;

  public void execute(String nfNumero, Long fornecedorId, List<ItemEntradaDTO> itens, Long pedidoId) {

    if (nfJaProcessada(nfNumero)) {
      throw new IllegalArgumentException("Nota Fiscal já registrada: " + nfNumero);
    }

    Fornecedor fornecedor = fornecedorService.buscarPorId(fornecedorId);
    if (fornecedor == null) {
      throw new IllegalArgumentException("Fornecedor não encontrado: " + fornecedorId);
    }

    Pedido pedido = null;
    if (pedidoId != null) {
      pedido = pedidoService.buscarPorId(pedidoId);
    }

    for (ItemEntradaDTO dto : itens) {
      Item item = itemService.buscarPorId(dto.getItemId());

      if (item == null) {
        item = new Item();
        item.setNome(dto.getNome());
        item.setQuantidadeTotal(0);
        item.setValidade(dto.getValidade());
        item.setIdEstoque(estoqueService.buscarEstoquePrincipal().getIdEstoque());
        itemService.salvar(item);
      }

      Date hoje = new Date();
      long diff = dto.getValidade().getTime() - hoje.getTime();
      long dias = diff / (1000 * 60 * 60 * 24);
      if (dias < 30) {
        System.out.println("Alerta: Item " + item.getNome() + " possui validade menor que 30 dias.");
      }

      estoqueService.somarQuantidade(item.getIdItem(), dto.getQuantidade());

      registrarMovimentacaoEntrada(item, dto.getQuantidade(), nfNumero, fornecedor);

      estoqueService.verificarEstoqueCritico(item.getIdItem());
    }

    if (pedido != null) {
      atualizarStatusPedido(pedido);
    }
  }

  private boolean nfJaProcessada(String nfNumero) {
    // logica de nf processadas dps vejo
    return false;
  }

  private void registrarMovimentacaoEntrada(Item item, int quantidade, String nfNumero, Fornecedor fornecedor) {
    System.out.printf(
        "Movimentação entrada registrada: ItemID=%d, Quantidade=%d, NF=%s, Fornecedor=%s, Data=%s%n",
        item.getIdItem(), quantidade, nfNumero, fornecedor.getCnpj(), new Date()
    );
  }

  private void atualizarStatusPedido(Pedido pedido) {
    boolean todosItensRecebidos = true; // calcular ainda verei
    pedido.setStatus(todosItensRecebidos ? "Recebido Totalmente" : "Recebido Parcialmente");
    pedidoService.salvar(pedido);
  }


}
