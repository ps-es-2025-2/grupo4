package com.simplehealth.cadastro.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDTO {

  private Long id;

  @NotBlank
  private String nomeCompleto;

  @NotNull
  private LocalDate dataNascimento;

  @NotBlank
  @Size(min = 11, max = 14)
  private String cpf;

  private String telefone;
  private String email;

  private Long convenioId;
  private String convenioNome;
}
