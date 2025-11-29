package com.simplehealth.agendamento.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicoNaoEncontradoExceptionTest {

  @Test
  void testExceptionMessage() {
    String message = "Médico com CRM CRM123456 não foi encontrado";
    MedicoNaoEncontradoException exception = new MedicoNaoEncontradoException(message);

    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionIsInstanceOfAgendamentoException() {
    MedicoNaoEncontradoException exception = new MedicoNaoEncontradoException("Teste");

    assertTrue(exception instanceof AgendamentoException);
  }

  @Test
  void testExceptionIsInstanceOfRuntimeException() {
    MedicoNaoEncontradoException exception = new MedicoNaoEncontradoException("Teste");

    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  void testExceptionWithDifferentMessages() {
    MedicoNaoEncontradoException exception1 = new MedicoNaoEncontradoException("Médico CRM123456 não encontrado");
    MedicoNaoEncontradoException exception2 = new MedicoNaoEncontradoException("Médico CRM789012 não encontrado");

    assertEquals("Médico CRM123456 não encontrado", exception1.getMessage());
    assertEquals("Médico CRM789012 não encontrado", exception2.getMessage());
    assertNotEquals(exception1.getMessage(), exception2.getMessage());
  }

  @Test
  void testExceptionCanBeThrown() {
    assertThrows(MedicoNaoEncontradoException.class, () -> {
      throw new MedicoNaoEncontradoException("Médico não existe");
    });
  }

  @Test
  void testExceptionMessageWithCRM() {
    String crm = "CRM987654";
    String message = String.format("Médico com CRM %s não foi encontrado", crm);
    MedicoNaoEncontradoException exception = new MedicoNaoEncontradoException(message);

    assertEquals("Médico com CRM CRM987654 não foi encontrado", exception.getMessage());
    assertTrue(exception.getMessage().contains(crm));
  }

  @Test
  void testExceptionInheritance() {
    MedicoNaoEncontradoException exception = new MedicoNaoEncontradoException("Teste");

    assertInstanceOf(AgendamentoException.class, exception);
    assertInstanceOf(RuntimeException.class, exception);
    assertInstanceOf(Exception.class, exception);
  }
}

