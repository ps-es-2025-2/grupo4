package com.simplehealth.cadastro.application.dto;

import com.simplehealth.cadastro.domain.enums.EPerfilUsuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {

  @Test
  void testUsuarioDTOCreation() {
    UsuarioDTO dto = new UsuarioDTO();
    dto.setId(1L);
    dto.setNomeCompleto("Admin User");
    dto.setLogin("admin");
    dto.setSenha("senha123");
    dto.setTelefone("11999999999");
    dto.setEmail("admin@email.com");
    dto.setPerfil(EPerfilUsuario.GESTOR);

    assertEquals(1L, dto.getId());
    assertEquals("Admin User", dto.getNomeCompleto());
    assertEquals("admin", dto.getLogin());
    assertEquals("senha123", dto.getSenha());
    assertEquals("11999999999", dto.getTelefone());
    assertEquals("admin@email.com", dto.getEmail());
    assertEquals(EPerfilUsuario.GESTOR, dto.getPerfil());
  }

  @Test
  void testUsuarioDTOBuilder() {
    UsuarioDTO dto = UsuarioDTO.builder()
        .id(2L)
        .nomeCompleto("Secretaria User")
        .login("secretaria")
        .senha("senha456")
        .telefone("11988888888")
        .email("secretaria@email.com")
        .perfil(EPerfilUsuario.SECRETARIA)
        .build();

    assertNotNull(dto);
    assertEquals(2L, dto.getId());
    assertEquals("Secretaria User", dto.getNomeCompleto());
    assertEquals("secretaria", dto.getLogin());
    assertEquals(EPerfilUsuario.SECRETARIA, dto.getPerfil());
  }

  @Test
  void testUsuarioDTONoArgsConstructor() {
    UsuarioDTO dto = new UsuarioDTO();
    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getNomeCompleto());
    assertNull(dto.getLogin());
    assertNull(dto.getPerfil());
  }

  @Test
  void testUsuarioDTOAllArgsConstructor() {
    UsuarioDTO dto = new UsuarioDTO(
        3L,
        "Financeiro User",
        "financeiro",
        "senha789",
        "11977777777",
        "financeiro@email.com",
        EPerfilUsuario.FINANCEIRO
    );

    assertEquals(3L, dto.getId());
    assertEquals("Financeiro User", dto.getNomeCompleto());
    assertEquals("financeiro", dto.getLogin());
    assertEquals(EPerfilUsuario.FINANCEIRO, dto.getPerfil());
  }
}

