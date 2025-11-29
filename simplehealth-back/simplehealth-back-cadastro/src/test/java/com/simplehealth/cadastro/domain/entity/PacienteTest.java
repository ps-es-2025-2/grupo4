package com.simplehealth.cadastro.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

  @Test
  void testPacienteCreation() {
    Paciente paciente = new Paciente();
    paciente.setId(1L);
    paciente.setNomeCompleto("João Silva");
    paciente.setCpf("12345678901");
    paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
    paciente.setTelefone("11999999999");
    paciente.setEmail("joao@email.com");

    assertEquals(1L, paciente.getId());
    assertEquals("João Silva", paciente.getNomeCompleto());
    assertEquals("12345678901", paciente.getCpf());
    assertEquals(LocalDate.of(1990, 1, 1), paciente.getDataNascimento());
    assertEquals("11999999999", paciente.getTelefone());
    assertEquals("joao@email.com", paciente.getEmail());
  }

  @Test
  void testPacienteBuilder() {
    Paciente paciente = Paciente.builder()
        .id(2L)
        .nomeCompleto("Maria Santos")
        .cpf("98765432100")
        .dataNascimento(LocalDate.of(1985, 5, 15))
        .telefone("11988888888")
        .email("maria@email.com")
        .build();

    assertNotNull(paciente);
    assertEquals(2L, paciente.getId());
    assertEquals("Maria Santos", paciente.getNomeCompleto());
    assertEquals("98765432100", paciente.getCpf());
  }

  @Test
  void testVerificarExistencia() {
    Paciente paciente = new Paciente();
    assertFalse(paciente.verificarExistencia());
  }
}

