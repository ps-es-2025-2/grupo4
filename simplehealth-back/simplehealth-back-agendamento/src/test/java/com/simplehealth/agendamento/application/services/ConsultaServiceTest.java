package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

  @Mock
  private ConsultaRepository consultaRepository;

  @InjectMocks
  private ConsultaService consultaService;

  private Consulta consulta;

  @BeforeEach
  void setUp() {
    consulta = new Consulta();
    consulta.setId("cons123");
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setDataHoraInicio(LocalDateTime.of(2025, 12, 1, 10, 0));
    consulta.setDataHoraFim(LocalDateTime.of(2025, 12, 1, 11, 0));
  }

  @Test
  void testSalvarConsulta() {
    when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = consultaService.salvar(consulta);

    assertNotNull(resultado);
    assertEquals("cons123", resultado.getId());
    assertEquals("12345678900", resultado.getPacienteCpf());
    verify(consultaRepository, times(1)).save(consulta);
  }

  @Test
  void testBuscarPorIdExistente() {
    when(consultaRepository.findById("cons123")).thenReturn(Optional.of(consulta));

    Optional<Consulta> resultado = consultaService.buscarPorId("cons123");

    assertTrue(resultado.isPresent());
    assertEquals("cons123", resultado.get().getId());
    verify(consultaRepository, times(1)).findById("cons123");
  }

  @Test
  void testBuscarPorIdNaoExistente() {
    when(consultaRepository.findById("inexistente")).thenReturn(Optional.empty());

    Optional<Consulta> resultado = consultaService.buscarPorId("inexistente");

    assertFalse(resultado.isPresent());
    verify(consultaRepository, times(1)).findById("inexistente");
  }

  @Test
  void testSalvarConsultaRetornaConsultaSalva() {
    when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = consultaService.salvar(consulta);

    assertNotNull(resultado);
    assertSame(consulta, resultado);
    verify(consultaRepository).save(consulta);
  }
}

