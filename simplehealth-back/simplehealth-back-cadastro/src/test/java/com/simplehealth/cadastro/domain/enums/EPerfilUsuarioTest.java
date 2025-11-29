package com.simplehealth.cadastro.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EPerfilUsuarioTest {

  @Test
  void testEnumValues() {
    assertEquals(5, EPerfilUsuario.values().length);
  }

  @Test
  void testEnumContainsMedico() {
    EPerfilUsuario perfil = EPerfilUsuario.MEDICO;
    assertNotNull(perfil);
    assertEquals("MEDICO", perfil.name());
  }

  @Test
  void testEnumContainsSecretaria() {
    EPerfilUsuario perfil = EPerfilUsuario.SECRETARIA;
    assertNotNull(perfil);
    assertEquals("SECRETARIA", perfil.name());
  }

  @Test
  void testEnumContainsGestor() {
    EPerfilUsuario perfil = EPerfilUsuario.GESTOR;
    assertNotNull(perfil);
    assertEquals("GESTOR", perfil.name());
  }

  @Test
  void testEnumContainsFinanceiro() {
    EPerfilUsuario perfil = EPerfilUsuario.FINANCEIRO;
    assertNotNull(perfil);
    assertEquals("FINANCEIRO", perfil.name());
  }

  @Test
  void testEnumContainsTesouraria() {
    EPerfilUsuario perfil = EPerfilUsuario.TESOURARIA;
    assertNotNull(perfil);
    assertEquals("TESOURARIA", perfil.name());
  }

  @Test
  void testValueOf() {
    EPerfilUsuario perfil = EPerfilUsuario.valueOf("MEDICO");
    assertEquals(EPerfilUsuario.MEDICO, perfil);
  }
}

