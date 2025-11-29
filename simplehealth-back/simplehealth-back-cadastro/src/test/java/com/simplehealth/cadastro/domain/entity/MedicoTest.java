package com.simplehealth.cadastro.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicoTest {

  @Test
  void testMedicoCreation() {
    Medico medico = new Medico();
    medico.setId(1L);
    medico.setNomeCompleto("Dr. Carlos Mendes");
    medico.setCrm("123456");
    medico.setEspecialidade("Ortopedia");
    medico.setTelefone("11977777777");
    medico.setEmail("carlos@email.com");

    assertEquals(1L, medico.getId());
    assertEquals("Dr. Carlos Mendes", medico.getNomeCompleto());
    assertEquals("123456", medico.getCrm());
    assertEquals("Ortopedia", medico.getEspecialidade());
    assertEquals("11977777777", medico.getTelefone());
    assertEquals("carlos@email.com", medico.getEmail());
  }

  @Test
  void testMedicoBuilder() {
    Medico medico = Medico.builder()
        .id(2L)
        .nomeCompleto("Dra. Ana Paula")
        .crm("654321")
        .especialidade("Cardiologia")
        .telefone("11966666666")
        .email("ana@email.com")
        .build();

    assertNotNull(medico);
    assertEquals(2L, medico.getId());
    assertEquals("Dra. Ana Paula", medico.getNomeCompleto());
    assertEquals("654321", medico.getCrm());
    assertEquals("Cardiologia", medico.getEspecialidade());
  }

  @Test
  void testMedicoNoArgsConstructor() {
    Medico medico = new Medico();
    assertNotNull(medico);
    assertNull(medico.getId());
    assertNull(medico.getCrm());
    assertNull(medico.getEspecialidade());
  }

  @Test
  void testMedicoAllArgsConstructor() {
    Medico medico = new Medico(3L, "987654", "Neurologia");
    assertNotNull(medico);
    assertEquals(3L, medico.getId());
    assertEquals("987654", medico.getCrm());
    assertEquals("Neurologia", medico.getEspecialidade());
  }
}

