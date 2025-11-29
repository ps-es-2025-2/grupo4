package com.simplehealth.cadastro.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicoDTOTest {

  @Test
  void testMedicoDTOCreation() {
    MedicoDTO dto = new MedicoDTO();
    dto.setId(1L);
    dto.setNomeCompleto("Dr. Carlos");
    dto.setCrm("123456");
    dto.setEspecialidade("Cardiologia");
    dto.setTelefone("11999999999");
    dto.setEmail("carlos@email.com");

    assertEquals(1L, dto.getId());
    assertEquals("Dr. Carlos", dto.getNomeCompleto());
    assertEquals("123456", dto.getCrm());
    assertEquals("Cardiologia", dto.getEspecialidade());
    assertEquals("11999999999", dto.getTelefone());
    assertEquals("carlos@email.com", dto.getEmail());
  }

  @Test
  void testMedicoDTOBuilder() {
    MedicoDTO dto = MedicoDTO.builder()
        .id(2L)
        .nomeCompleto("Dra. Ana")
        .crm("654321")
        .especialidade("Ortopedia")
        .telefone("11988888888")
        .email("ana@email.com")
        .build();

    assertNotNull(dto);
    assertEquals(2L, dto.getId());
    assertEquals("Dra. Ana", dto.getNomeCompleto());
    assertEquals("654321", dto.getCrm());
    assertEquals("Ortopedia", dto.getEspecialidade());
  }

  @Test
  void testMedicoDTONoArgsConstructor() {
    MedicoDTO dto = new MedicoDTO();
    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getNomeCompleto());
    assertNull(dto.getCrm());
  }

  @Test
  void testMedicoDTOAllArgsConstructor() {
    MedicoDTO dto = new MedicoDTO(
        3L,
        "Dr. Pedro",
        "789456",
        "Neurologia",
        "11977777777",
        "pedro@email.com"
    );

    assertEquals(3L, dto.getId());
    assertEquals("Dr. Pedro", dto.getNomeCompleto());
    assertEquals("789456", dto.getCrm());
    assertEquals("Neurologia", dto.getEspecialidade());
  }
}

