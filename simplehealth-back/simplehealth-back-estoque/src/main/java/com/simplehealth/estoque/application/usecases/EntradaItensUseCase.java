package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.dto.ItemEntradaDTO;
import com.simplehealth.estoque.application.service.EstoqueService;
import com.simplehealth.estoque.application.service.FornecedorService;
import com.simplehealth.estoque.application.service.ItemService;
import com.simplehealth.estoque.application.service.PedidoService;
import com.simplehealth.estoque.domain.entity.Alimento;
import com.simplehealth.estoque.domain.entity.Fornecedor;
import com.simplehealth.estoque.domain.entity.Hospitalar;
import com.simplehealth.estoque.domain.entity.Item;
import com.simplehealth.estoque.domain.entity.Medicamento;
import com.simplehealth.estoque.domain.entity.Pedido;
import com.simplehealth.estoque.domain.enums.TipoItem;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntradaItensUseCase {

  private final EstoqueService estoqueService;
  private final ItemService itemService;
  private final FornecedorService fornecedorService;
  private final PedidoService pedidoService;

  public void execute(String nfNumero, UUID fornecedorId, List<ItemEntradaDTO> itens, UUID pedidoId) {

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
      Item item = null;

      if (dto.getItemId() != null && dto.getTipo() != null) {
        try {
          item = itemService.buscarPorId(dto.getItemId(), dto.getTipo());
        } catch (IllegalArgumentException e) {
        }
      }

      if (item == null) {
        if (dto.getTipo() == null) {
          throw new IllegalArgumentException("Tipo de item é obrigatório para novos itens.");
        }

        switch (dto.getTipo()) {
          case ALIMENTO:
            Alimento alimento = new Alimento();
            alimento.setIdItem(UUID.randomUUID());
            alimento.setNome(dto.getNome());
            alimento.setQuantidadeTotal(0);
            alimento.setValidade(dto.getValidade());
            alimento.setIdEstoque(estoqueService.buscarEstoquePrincipal().getIdEstoque());
            item = alimento;
            break;
          case MEDICAMENTO:
            Medicamento medicamento = new Medicamento();
            medicamento.setIdItem(UUID.randomUUID());
            medicamento.setNome(dto.getNome());
            medicamento.setQuantidadeTotal(0);
            medicamento.setValidade(dto.getValidade());
            medicamento.setIdEstoque(estoqueService.buscarEstoquePrincipal().getIdEstoque());
            item = medicamento;
            break;
          case HOSPITALAR:
            Hospitalar hospitalar = new Hospitalar();
            hospitalar.setIdItem(UUID.randomUUID());
            hospitalar.setNome(dto.getNome());
            hospitalar.setQuantidadeTotal(0);
            hospitalar.setValidade(dto.getValidade());
            hospitalar.setIdEstoque(estoqueService.buscarEstoquePrincipal().getIdEstoque());
            item = hospitalar;
            break;
          default:
            throw new IllegalArgumentException("Tipo de item inválido: " + dto.getTipo());
        }
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
        item.getIdItem(), quantidade, nfNumero, fornecedor.getCnpj(), new Date());
  }

  private void atualizarStatusPedido(Pedido pedido) {
    boolean todosItensRecebidos = true; // calcular ainda verei
    pedido.setStatus(todosItensRecebidos ? "Recebido Totalmente" : "Recebido Parcialmente");
    pedidoService.salvar(pedido);
  }

}
