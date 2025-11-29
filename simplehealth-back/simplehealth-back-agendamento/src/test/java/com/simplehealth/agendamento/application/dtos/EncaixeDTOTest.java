package com.simplehealth.agendamento.application.dtos;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EncaixeDTOTest {

  @Test
  void testEncaixeDTOCreationWithBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 15, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 16, 0);

    EncaixeDTO encaixeDTO = EncaixeDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .motivoEncaixe("Urgência médica")
        .observacoes("Paciente com dor aguda")
        .usuarioCriadorLogin("admin")
        .build();

    assertNotNull(encaixeDTO);
    assertEquals("12345678900", encaixeDTO.getPacienteCpf());
    assertEquals("CRM123456", encaixeDTO.getMedicoCrm());
    assertEquals(inicio, encaixeDTO.getDataHoraInicio());
    assertEquals(fim, encaixeDTO.getDataHoraFim());
    assertEquals("Urgência médica", encaixeDTO.getMotivoEncaixe());
    assertEquals("Paciente com dor aguda", encaixeDTO.getObservacoes());
    assertEquals("admin", encaixeDTO.getUsuarioCriadorLogin());
  }

  @Test
  void testEncaixeDTONoArgsConstructor() {
    EncaixeDTO encaixeDTO = new EncaixeDTO();

    assertNull(encaixeDTO.getPacienteCpf());
    assertNull(encaixeDTO.getMedicoCrm());
    assertNull(encaixeDTO.getDataHoraInicio());
    assertNull(encaixeDTO.getDataHoraFim());
    assertNull(encaixeDTO.getMotivoEncaixe());
    assertNull(encaixeDTO.getObservacoes());
    assertNull(encaixeDTO.getUsuarioCriadorLogin());
  }

  @Test
  void testEncaixeDTOAllArgsConstructor() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 2, 9, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 2, 10, 0);

    EncaixeDTO encaixeDTO = new EncaixeDTO(
        "98765432100",
        "CRM789012",
        inicio,
        fim,
        "Retorno urgente",
        "Acompanhamento pós-operatório",
        "recepcionista"
    );

    assertEquals("98765432100", encaixeDTO.getPacienteCpf());
    assertEquals("CRM789012", encaixeDTO.getMedicoCrm());
    assertEquals(inicio, encaixeDTO.getDataHoraInicio());
    assertEquals(fim, encaixeDTO.getDataHoraFim());
    assertEquals("Retorno urgente", encaixeDTO.getMotivoEncaixe());
    assertEquals("Acompanhamento pós-operatório", encaixeDTO.getObservacoes());
    assertEquals("recepcionista", encaixeDTO.getUsuarioCriadorLogin());
  }

  @Test
  void testEncaixeDTOSettersAndGetters() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 3, 11, 30);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 3, 12, 30);

    EncaixeDTO encaixeDTO = new EncaixeDTO();
    encaixeDTO.setPacienteCpf("11122233344");
    encaixeDTO.setMedicoCrm("CRM456789");
    encaixeDTO.setDataHoraInicio(inicio);
    encaixeDTO.setDataHoraFim(fim);
    encaixeDTO.setMotivoEncaixe("Emergência");
    encaixeDTO.setObservacoes("Febre alta");
    encaixeDTO.setUsuarioCriadorLogin("enfermeiro");

    assertEquals("11122233344", encaixeDTO.getPacienteCpf());
    assertEquals("CRM456789", encaixeDTO.getMedicoCrm());
    assertEquals(inicio, encaixeDTO.getDataHoraInicio());
    assertEquals(fim, encaixeDTO.getDataHoraFim());
    assertEquals("Emergência", encaixeDTO.getMotivoEncaixe());
    assertEquals("Febre alta", encaixeDTO.getObservacoes());
    assertEquals("enfermeiro", encaixeDTO.getUsuarioCriadorLogin());
  }

  @Test
  void testEncaixeDTOWithNullValues() {
    EncaixeDTO encaixeDTO = EncaixeDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .build();

    assertEquals("12345678900", encaixeDTO.getPacienteCpf());
    assertEquals("CRM123456", encaixeDTO.getMedicoCrm());
    assertNull(encaixeDTO.getDataHoraInicio());
    assertNull(encaixeDTO.getDataHoraFim());
    assertNull(encaixeDTO.getMotivoEncaixe());
    assertNull(encaixeDTO.getObservacoes());
    assertNull(encaixeDTO.getUsuarioCriadorLogin());
  }

  @Test
  void testEncaixeDTOEqualsAndHashCode() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 15, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 16, 0);

    EncaixeDTO encaixe1 = EncaixeDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .build();

    EncaixeDTO encaixe2 = EncaixeDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .build();

    assertEquals(encaixe1, encaixe2);
    assertEquals(encaixe1.hashCode(), encaixe2.hashCode());
  }
}

