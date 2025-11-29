package com.simplehealth.cadastro.application.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacienteDTOTest {

  @Test
  void testPacienteDTOCreation() {
    PacienteDTO dto = new PacienteDTO();
    dto.setId(1L);
    dto.setNomeCompleto("João Silva");
    dto.setCpf("12345678901");
    dto.setDataNascimento(LocalDate.of(1990, 1, 1));
    dto.setTelefone("11999999999");
    dto.setEmail("joao@email.com");

    assertEquals(1L, dto.getId());
    assertEquals("João Silva", dto.getNomeCompleto());
    assertEquals("12345678901", dto.getCpf());
    assertEquals(LocalDate.of(1990, 1, 1), dto.getDataNascimento());
    assertEquals("11999999999", dto.getTelefone());
    assertEquals("joao@email.com", dto.getEmail());
  }

  @Test
  void testPacienteDTOBuilder() {
    PacienteDTO dto = PacienteDTO.builder()
        .id(2L)
        .nomeCompleto("Maria Santos")
        .cpf("98765432100")
        .dataNascimento(LocalDate.of(1985, 5, 15))
        .telefone("11988888888")
        .email("maria@email.com")
        .build();

    assertNotNull(dto);
    assertEquals(2L, dto.getId());
    assertEquals("Maria Santos", dto.getNomeCompleto());
    assertEquals("98765432100", dto.getCpf());
  }

  @Test
  void testPacienteDTONoArgsConstructor() {
    PacienteDTO dto = new PacienteDTO();
    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getNomeCompleto());
  }

  @Test
  void testPacienteDTOAllArgsConstructor() {
    PacienteDTO dto = new PacienteDTO(
        3L,
        "Carlos Oliveira",
        LocalDate.of(1995, 3, 20),
        "11122233344",
        "11977777777",
        "carlos@email.com"
    );

    assertEquals(3L, dto.getId());
    assertEquals("Carlos Oliveira", dto.getNomeCompleto());
    assertEquals("11122233344", dto.getCpf());
  }
}

