package com.simplehealth.cadastro.domain.entity;

import com.simplehealth.cadastro.domain.enums.EPerfilUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Usuario extends Pessoa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String login;
  private String senha; //hash

  @Enumerated(EnumType.STRING)
  private EPerfilUsuario perfil;

  public boolean temPermissaoEncaixe() {
    return perfil == EPerfilUsuario.MEDICO || perfil == EPerfilUsuario.SECRETARIA;
  }
}
