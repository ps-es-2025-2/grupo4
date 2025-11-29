package com.simplehealth.agendamento.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoExceptionTest {

  @Test
  void testExceptionMessage() {
    String message = "Erro no agendamento";
    AgendamentoException exception = new AgendamentoException(message);

    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionIsInstanceOfRuntimeException() {
    AgendamentoException exception = new AgendamentoException("Teste");

    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  void testExceptionWithDifferentMessages() {
    AgendamentoException exception1 = new AgendamentoException("Horário indisponível");
    AgendamentoException exception2 = new AgendamentoException("Conflito de agendamento");

    assertEquals("Horário indisponível", exception1.getMessage());
    assertEquals("Conflito de agendamento", exception2.getMessage());
    assertNotEquals(exception1.getMessage(), exception2.getMessage());
  }

  @Test
  void testExceptionCanBeThrown() {
    assertThrows(AgendamentoException.class, () -> {
      throw new AgendamentoException("Erro ao agendar");
    });
  }

  @Test
  void testExceptionAsBaseClass() {
    AgendamentoException baseException = new AgendamentoException("Erro genérico");
    UsuarioNaoEncontradoException usuarioException = new UsuarioNaoEncontradoException("Usuário não encontrado");

    assertTrue(baseException instanceof RuntimeException);
    assertTrue(usuarioException instanceof AgendamentoException);
  }

  @Test
  void testExceptionInheritance() {
    AgendamentoException exception = new AgendamentoException("Teste");

    assertInstanceOf(RuntimeException.class, exception);
    assertInstanceOf(Exception.class, exception);
  }

  @Test
  void testCommonExceptionScenarios() {
    AgendamentoException horarioIndisponivel = new AgendamentoException("Horário indisponível");
    AgendamentoException horarioBloqueado = new AgendamentoException("Horário bloqueado");
    AgendamentoException conflito = new AgendamentoException("Conflito de horário");

    assertTrue(horarioIndisponivel.getMessage().contains("indisponível"));
    assertTrue(horarioBloqueado.getMessage().contains("bloqueado"));
    assertTrue(conflito.getMessage().contains("Conflito"));
  }

  @Test
  void testExceptionWithDetailedMessage() {
    AgendamentoException exception = new AgendamentoException(
        "Não é possível agendar: médico já possui consulta marcada para este horário"
    );

    assertTrue(exception.getMessage().contains("Não é possível agendar"));
    assertTrue(exception.getMessage().contains("médico"));
    assertTrue(exception.getMessage().contains("consulta marcada"));
  }

  @Test
  void testExceptionCanBeCaught() {
    try {
      throw new AgendamentoException("Teste de captura");
    } catch (AgendamentoException e) {
      assertEquals("Teste de captura", e.getMessage());
    }
  }
}

