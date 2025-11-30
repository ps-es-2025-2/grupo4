package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.dto.EntradaItensInput;
import com.simplehealth.estoque.application.usecases.ControlarValidadeUseCase;
import com.simplehealth.estoque.application.usecases.DarBaixaInsumosUseCase;
import com.simplehealth.estoque.application.usecases.EntradaItensUseCase;
import com.simplehealth.estoque.domain.entity.Item;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controle")
@RequiredArgsConstructor
public class EstoqueController {

  private final ControlarValidadeUseCase controlarValidadeUseCase;
  private final DarBaixaInsumosUseCase darBaixaInsumosUseCase;
  private final EntradaItensUseCase entradaItensUseCase;

  // UC10: Controle de validade
  @GetMapping("/validade")
  public List<Item> verificarValidade(
      @RequestParam int dias,
      @RequestParam(defaultValue = "false") boolean incluirVencidos,
      @RequestParam(defaultValue = "false") boolean descartarItens,
      @RequestParam(required = false) String codigoCusto
  ) {
    return controlarValidadeUseCase.execute(dias, incluirVencidos, descartarItens, codigoCusto);
  }

  // UC05: Baixa de Insumos
  @PostMapping("/baixa")
  public void darBaixa(
      @RequestParam Long itemId,
      @RequestParam int quantidade,
      @RequestParam String destinoConsumo
  ) {
    darBaixaInsumosUseCase.execute(itemId, quantidade, destinoConsumo);
  }


  // UC06: Entrada de NF/Itens
  @PostMapping("/entrada")
  public void entradaDeItens(@RequestBody EntradaItensInput input) {
    entradaItensUseCase.execute(
        input.getNfNumero(),
        input.getFornecedorId(),
        input.getItens(),
        input.getPedidoId()
    );
  }

}
