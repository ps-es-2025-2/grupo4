package com.simplehealth.estoque.application.usecases;

import com.simplehealth.estoque.application.dto.ControleValidadeDTO;
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

  public List<Item> execute(ControleValidadeDTO dto) {
    if (dto.getDiasAntecedencia() == null) {
      dto.setDiasAntecedencia(30); // Default: 30 dias
    }
    if (dto.getIncluirVencidos() == null) {
      dto.setIncluirVencidos(false);
    }
    if (dto.getDescartarItens() == null) {
      dto.setDescartarItens(false);
    }

    if (dto.getDescartarItens() && (dto.getCodigoCusto() == null || dto.getCodigoCusto().trim().isEmpty())) {
      throw new IllegalArgumentException(
          "Código de custo é obrigatório para descarte de itens (RN-ESTOQUE.4). " +
              "Informe o código de custo no DTO.");
    }

    List<Item> itensParaControle = buscarItensPorValidade(
        dto.getDiasAntecedencia(),
        dto.getIncluirVencidos());

    if (itensParaControle.isEmpty()) {
      System.out.println(
          "Nenhum item encontrado no critério de validade " +
              "(dias: " + dto.getDiasAntecedencia() + ", incluir vencidos: " + dto.getIncluirVencidos() + ").");
      return itensParaControle;
    }

    if (dto.getDescartarItens()) {
      descartarItens(itensParaControle, dto.getCodigoCusto());
    }

    return itensParaControle;
  }

  private List<Item> buscarItensPorValidade(int diasAntecedencia, boolean incluirVencidos) {
    List<Item> todosItens = itemService.listarTodos();
    Date hoje = new Date();
    List<Item> resultado = new ArrayList<>();
    int itensSemValidade = 0;

    for (Item item : todosItens) {
      // Tratar itens sem validade configurada
      if (item.getValidade() == null) {
        itensSemValidade++;
        continue;
      }

      long diff = item.getValidade().getTime() - hoje.getTime();
      long dias = diff / (1000 * 60 * 60 * 24);

      if ((incluirVencidos && dias < 0) || (dias >= 0 && dias <= diasAntecedencia)) {
        resultado.add(item);
      }
    }

    if (itensSemValidade > 0) {
      System.out.println(
          "AVISO: " + itensSemValidade + " item(ns) sem data de validade configurada foram ignorados.");
    }

    return resultado;
  }

  private void descartarItens(List<Item> itens, String codigoCusto) {
    Date dataAtual = new Date();
    int totalDescartado = 0;

    System.out.println("=== DESCARTE DE ITENS ===");
    System.out.println("Código de Custo: " + codigoCusto + " | Data: " + dataAtual);

    for (Item item : itens) {
      int qtde = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
      if (qtde <= 0) {
        continue;
      }

      item.setQuantidadeTotal(0);
      itemService.salvar(item);

      registrarMovimentacaoDescarte(item, qtde, codigoCusto, dataAtual);

      totalDescartado++;
    }

    System.out.println("Total de itens descartados: " + totalDescartado);
    System.out.println("=== FIM DESCARTE ===\n");
  }

  private void registrarMovimentacaoDescarte(Item item, int quantidade, String codigoCusto, Date data) {
    System.out.println(
        "  [DESCARTE] Item: " + item.getNome() +
            " | Qtd: " + quantidade +
            " | Código de Custo: " + codigoCusto +
            " | Validade: " + item.getValidade());
  }
}
