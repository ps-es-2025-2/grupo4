package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.dto.ItemDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EntradaItensUseCase {

  private static final Set<String> nfsProcessadas = new HashSet<>();
  private final EstoqueService estoqueService;
  private final ItemService itemService;
  private final FornecedorService fornecedorService;
  private final PedidoService pedidoService;

  @Transactional
  public List<Item> execute(String nfNumero, UUID fornecedorId, List<ItemDTO> itens, UUID pedidoId) {

    if (nfsProcessadas.contains(nfNumero)) {
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

    System.out.println("ENTRADA DE NF");
    System.out.println("NF: " + nfNumero + " | Fornecedor: " + fornecedor.getCnpj() +
        " | Data: " + new Date());

    List<Item> itensProcessados = new ArrayList<>();

    for (ItemDTO dto : itens) {
      Item item = processarItemEntrada(dto, nfNumero, fornecedor);
      itensProcessados.add(item);
    }

    if (pedido != null) {
      atualizarStatusPedido(pedido);
    }

    for (ItemDTO dto : itens) {
      if (dto.getItemId() != null) {
        estoqueService.verificarEstoqueCritico(dto.getItemId());
      }
    }

    nfsProcessadas.add(nfNumero);

    System.out.println("=== FIM ENTRADA DE NF ===\n");

    return itensProcessados;
  }


  private Item processarItemEntrada(ItemDTO dto, String nfNumero, Fornecedor fornecedor) {
    Item item = null;

    if (dto.getItemId() != null && dto.getTipo() != null) {
      try {
        item = itemService.buscarPorId(dto.getItemId(), dto.getTipo());
      } catch (IllegalArgumentException e) {
        // ignora, item será criado
      }
    }

    if (item == null) {
      item = criarNovoItem(dto);
      itemService.salvar(item);
      System.out.println("  [NOVO ITEM] " + item.getNome() + " (ID: " + item.getIdItem() + ")");
    }

    validarValidade(dto, item);

    int quantidadeAnterior = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    estoqueService.somarQuantidade(item.getIdItem(), dto.getQuantidade());

    registrarMovimentacaoEntrada(
        item,
        dto.getQuantidade(),
        quantidadeAnterior,
        nfNumero,
        fornecedor,
        dto.getLote()
    );

    return item;
  }

  private Item criarNovoItem(ItemDTO dto) {
    if (dto.getTipo() == null) {
      throw new IllegalArgumentException("Tipo de item é obrigatório para novos itens.");
    }

    Item item;

    switch (dto.getTipo()) {
      case ALIMENTO:
        Alimento alimento = new Alimento();
        alimento.setIdItem(UUID.randomUUID());
        alimento.setNome(dto.getNome());
        alimento.setQuantidadeTotal(0);
        alimento.setValidade(dto.getValidade());
        item = alimento;
        break;

      case MEDICAMENTO:
        Medicamento medicamento = new Medicamento();
        medicamento.setIdItem(UUID.randomUUID());
        medicamento.setNome(dto.getNome());
        medicamento.setQuantidadeTotal(0);
        medicamento.setValidade(dto.getValidade());
        item = medicamento;
        break;

      case HOSPITALAR:
        Hospitalar hospitalar = new Hospitalar();
        hospitalar.setIdItem(UUID.randomUUID());
        hospitalar.setNome(dto.getNome());
        hospitalar.setQuantidadeTotal(0);
        hospitalar.setValidade(dto.getValidade());
        item = hospitalar;
        break;

      default:
        throw new IllegalArgumentException("Tipo de item inválido: " + dto.getTipo());
    }

    return item;
  }

  private void validarValidade(ItemDTO dto, Item item) {
    if (dto.getValidade() == null) {
      return;
    }

    Date hoje = new Date();
    long diff = dto.getValidade().getTime() - hoje.getTime();
    long dias = diff / (1000 * 60 * 60 * 24);

    if (dias < 30) {
      if (dto.getConfirmacaoGestor() == null || !dto.getConfirmacaoGestor()) {
        throw new IllegalArgumentException(
            "Item '" + item.getNome() + "' possui validade menor que 30 dias (" + dias + " dias). " +
                "Confirmação do Gestor é obrigatória. Defina confirmacaoGestor=true no DTO.");
      }
      System.out.println(
          "  [ALERTA] Item '" + item.getNome() + "' com validade curta (" + dias + " dias) " +
              "aprovado pelo Gestor.");
    }
  }

  private void registrarMovimentacaoEntrada(Item item, int quantidade, int quantidadeAnterior,
      String nfNumero, Fornecedor fornecedor, String lote) {
    System.out.println(
        "  [ENTRADA] Item: " + item.getNome() +
            " | Qtd: " + quantidade +
            " | Saldo: " + quantidadeAnterior + " → " + (quantidadeAnterior + quantidade) +
            " | Lote: " + (lote != null ? lote : "N/A") +
            " | NF: " + nfNumero +
            " | Fornecedor: " + fornecedor.getCnpj());
  }

  private void atualizarStatusPedido(Pedido pedido) {
    if (pedido.getItemIds() == null || pedido.getItemIds().isEmpty()) {
      pedido.setStatus("Recebido Totalmente");
      pedidoService.salvar(pedido);
      return;
    }

    boolean todosItensRecebidos = false;
    pedido.setStatus(todosItensRecebidos ? "Recebido Totalmente" : "Recebido Parcialmente");
    pedidoService.salvar(pedido);
  }
}
