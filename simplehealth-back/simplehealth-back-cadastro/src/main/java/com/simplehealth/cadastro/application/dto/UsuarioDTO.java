package com.simplehealth.cadastro.application.dto;

import com.simplehealth.cadastro.domain.enums.EPerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

  private Long id;

  @NotBlank
  private String nomeCompleto;

  @NotBlank
  private String login;

  @NotBlank
  private String senha;

  private String telefone;
  private String email;

  @NotNull
  private EPerfilUsuario perfil;
}
