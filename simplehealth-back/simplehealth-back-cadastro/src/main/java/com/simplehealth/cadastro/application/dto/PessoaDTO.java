package com.simplehealth.cadastro.application.dto;

import java.time.LocalDate;

public record PessoaDTO(
    Long id,
    String nome,
    String cpf,
    String email,
    LocalDate dataNascimento,
    String tipo
) {

}
