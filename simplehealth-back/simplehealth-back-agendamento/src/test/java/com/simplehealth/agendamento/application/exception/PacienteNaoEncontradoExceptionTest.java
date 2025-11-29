package com.simplehealth.agendamento.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacienteNaoEncontradoExceptionTest {

  @Test
  void testExceptionMessage() {
    String message = "Paciente com CPF 12345678900 não foi encontrado";
    PacienteNaoEncontradoException exception = new PacienteNaoEncontradoException(message);

    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionIsInstanceOfAgendamentoException() {
    PacienteNaoEncontradoException exception = new PacienteNaoEncontradoException("Teste");

    assertTrue(exception instanceof AgendamentoException);
  }

  @Test
  void testExceptionIsInstanceOfRuntimeException() {
    PacienteNaoEncontradoException exception = new PacienteNaoEncontradoException("Teste");

    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  void testExceptionWithDifferentMessages() {
    PacienteNaoEncontradoException exception1 = new PacienteNaoEncontradoException("Paciente 12345678900 não encontrado");
    PacienteNaoEncontradoException exception2 = new PacienteNaoEncontradoException("Paciente 98765432100 não encontrado");

    assertEquals("Paciente 12345678900 não encontrado", exception1.getMessage());
    assertEquals("Paciente 98765432100 não encontrado", exception2.getMessage());
    assertNotEquals(exception1.getMessage(), exception2.getMessage());
  }

  @Test
  void testExceptionCanBeThrown() {
    assertThrows(PacienteNaoEncontradoException.class, () -> {
      throw new PacienteNaoEncontradoException("Paciente não existe");
    });
  }

  @Test
  void testExceptionMessageWithCPF() {
    String cpf = "11111111111";
    String message = String.format("Paciente com CPF %s não foi encontrado", cpf);
    PacienteNaoEncontradoException exception = new PacienteNaoEncontradoException(message);

    assertEquals("Paciente com CPF 11111111111 não foi encontrado", exception.getMessage());
    assertTrue(exception.getMessage().contains(cpf));
  }

  @Test
  void testExceptionInheritance() {
    PacienteNaoEncontradoException exception = new PacienteNaoEncontradoException("Teste");

    assertInstanceOf(AgendamentoException.class, exception);
    assertInstanceOf(RuntimeException.class, exception);
    assertInstanceOf(Exception.class, exception);
  }

  @Test
  void testExceptionWithFormattedCPF() {
    String cpf = "123.456.789-00";
    PacienteNaoEncontradoException exception = new PacienteNaoEncontradoException(
        "Paciente com CPF " + cpf + " não existe no sistema"
    );

    assertTrue(exception.getMessage().contains(cpf));
    assertTrue(exception.getMessage().contains("não existe no sistema"));
  }
}

