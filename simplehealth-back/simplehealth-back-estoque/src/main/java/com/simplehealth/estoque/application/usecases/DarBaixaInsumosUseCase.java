package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.dto.BaixaInsumoDTO;
import com.simplehealth.estoque.application.dto.BaixaInsumoResponse;
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

  public BaixaInsumoResponse execute(BaixaInsumoDTO dto) {

    if (dto.getDestinoConsumo() == null || dto.getDestinoConsumo().trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Destino do consumo é obrigatório para rastreabilidade (RN-BAIXA.1).");
    }

    Item item = itemService.buscarPorId(dto.getItemId());
    if (item == null) {
      throw new IllegalArgumentException("Item não encontrado: " + dto.getItemId());
    }

    Date hoje = new Date();
    if (item.getValidade() != null && !item.getValidade().after(hoje)) {
      throw new IllegalArgumentException(
          "Item '" + item.getNome() + "' está vencido (validade: " + item.getValidade() + ").");
    }

    int saldoAnterior = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
    if (saldoAnterior < dto.getQuantidadeNecessaria()) {
      throw new IllegalArgumentException(
          "Estoque insuficiente. Disponível: " + saldoAnterior +
              ", Solicitado: " + dto.getQuantidadeNecessaria());
    }

    String loteUtilizado = dto.getLote() != null ? dto.getLote() : "LOTE_UNICO";

    estoqueService.darBaixa(dto.getItemId(), dto.getQuantidadeNecessaria());

    int saldoAtual = saldoAnterior - dto.getQuantidadeNecessaria();

    System.out.println(
        "[SAÍDA] Item: " + item.getNome() +
            " | Qtd: " + dto.getQuantidadeNecessaria() +
            " | Saldo: " + saldoAnterior + " → " + saldoAtual +
            " | Lote: " + loteUtilizado +
            " | Destino: " + dto.getDestinoConsumo() +
            " | Data: " + new Date());

    boolean estoqueCritico = estoqueService.verificarEstoqueCritico(dto.getItemId());

    return new BaixaInsumoResponse(
        item.getIdItem(),
        item.getNome(),
        dto.getQuantidadeNecessaria(),
        saldoAnterior,
        saldoAtual,
        loteUtilizado,
        estoqueCritico
    );
  }
}
