package com.simplehealth.cadastro.domain.entity;

import com.simplehealth.cadastro.domain.enums.EPerfilUsuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

  @Test
  void testUsuarioCreation() {
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNomeCompleto("Admin User");
    usuario.setLogin("admin");
    usuario.setSenha("hash123");
    usuario.setPerfil(EPerfilUsuario.GESTOR);
    usuario.setTelefone("11955555555");
    usuario.setEmail("admin@email.com");

    assertEquals(1L, usuario.getId());
    assertEquals("Admin User", usuario.getNomeCompleto());
    assertEquals("admin", usuario.getLogin());
    assertEquals("hash123", usuario.getSenha());
    assertEquals(EPerfilUsuario.GESTOR, usuario.getPerfil());
  }

  @Test
  void testUsuarioBuilder() {
    Usuario usuario = Usuario.builder()
        .id(2L)
        .nomeCompleto("Secretaria User")
        .login("secretaria")
        .senha("hash456")
        .perfil(EPerfilUsuario.SECRETARIA)
        .telefone("11944444444")
        .email("secretaria@email.com")
        .build();

    assertNotNull(usuario);
    assertEquals(2L, usuario.getId());
    assertEquals("Secretaria User", usuario.getNomeCompleto());
    assertEquals(EPerfilUsuario.SECRETARIA, usuario.getPerfil());
  }

  @Test
  void testTemPermissaoEncaixe_Medico() {
    Usuario usuario = new Usuario();
    usuario.setPerfil(EPerfilUsuario.MEDICO);

    assertTrue(usuario.temPermissaoEncaixe());
  }

  @Test
  void testTemPermissaoEncaixe_Secretaria() {
    Usuario usuario = new Usuario();
    usuario.setPerfil(EPerfilUsuario.SECRETARIA);

    assertTrue(usuario.temPermissaoEncaixe());
  }

  @Test
  void testTemPermissaoEncaixe_Gestor() {
    Usuario usuario = new Usuario();
    usuario.setPerfil(EPerfilUsuario.GESTOR);

    assertFalse(usuario.temPermissaoEncaixe());
  }

  @Test
  void testTemPermissaoEncaixe_Financeiro() {
    Usuario usuario = new Usuario();
    usuario.setPerfil(EPerfilUsuario.FINANCEIRO);

    assertFalse(usuario.temPermissaoEncaixe());
  }

  @Test
  void testTemPermissaoEncaixe_Tesouraria() {
    Usuario usuario = new Usuario();
    usuario.setPerfil(EPerfilUsuario.TESOURARIA);

    assertFalse(usuario.temPermissaoEncaixe());
  }
}

