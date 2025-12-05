package com.simplehealth.estoque.application.dto;

import java.util.UUID;

public record BaixaInsumoResponse(
    UUID itemId,
    String nome,
    int quantidadeBaixada,
    int saldoAnterior,
    int saldoAtual,
    String lote,
    boolean estoqueCritico
) {

}
