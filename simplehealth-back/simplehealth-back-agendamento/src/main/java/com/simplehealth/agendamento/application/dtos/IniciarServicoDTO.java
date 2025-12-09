package com.simplehealth.agendamento.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IniciarServicoDTO {

  private String id;
  private String usuarioLogin;
}
