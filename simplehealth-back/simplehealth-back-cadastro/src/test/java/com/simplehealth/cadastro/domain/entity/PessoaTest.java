package com.simplehealth.cadastro.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

  @Test
  void testPacienteCreationWithBuilder() {
    Paciente paciente = Paciente.builder()
        .id(1L)
        .nomeCompleto("João Silva")
        .telefone("11999999999")
        .email("joao@email.com")
        .build();

    assertNotNull(paciente);
    assertEquals(1L, paciente.getId());
    assertEquals("João Silva", paciente.getNomeCompleto());
    assertEquals("11999999999", paciente.getTelefone());
    assertEquals("joao@email.com", paciente.getEmail());
  }

  @Test
  void testMedicoCreationWithBuilder() {
    Medico medico = Medico.builder()
        .id(1L)
        .nomeCompleto("Dr. Maria Santos")
        .telefone("11988888888")
        .email("maria@email.com")
        .crm("123456")
        .especialidade("Cardiologia")
        .build();

    assertNotNull(medico);
    assertEquals(1L, medico.getId());
    assertEquals("Dr. Maria Santos", medico.getNomeCompleto());
    assertEquals("123456", medico.getCrm());
    assertEquals("Cardiologia", medico.getEspecialidade());
  }

  @Test
  void testUsuarioCreationWithBuilder() {
    Usuario usuario = Usuario.builder()
        .id(1L)
        .nomeCompleto("Carlos Admin")
        .telefone("11977777777")
        .email("carlos@email.com")
        .login("carlos.admin")
        .senha("senha123")
        .build();

    assertNotNull(usuario);
    assertEquals(1L, usuario.getId());
    assertEquals("Carlos Admin", usuario.getNomeCompleto());
    assertEquals("carlos.admin", usuario.getLogin());
  }
}

