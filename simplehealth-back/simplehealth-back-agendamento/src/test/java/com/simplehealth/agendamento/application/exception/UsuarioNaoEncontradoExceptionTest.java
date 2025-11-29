package com.simplehealth.agendamento.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioNaoEncontradoExceptionTest {

  @Test
  void testExceptionMessage() {
    String message = "Usuário com login admin não foi encontrado";
    UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(message);

    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionIsInstanceOfAgendamentoException() {
    UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException("Teste");

    assertTrue(exception instanceof AgendamentoException);
  }

  @Test
  void testExceptionIsInstanceOfRuntimeException() {
    UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException("Teste");

    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  void testExceptionWithDifferentMessages() {
    UsuarioNaoEncontradoException exception1 = new UsuarioNaoEncontradoException("Usuário admin não encontrado");
    UsuarioNaoEncontradoException exception2 = new UsuarioNaoEncontradoException("Usuário recepcionista não encontrado");

    assertEquals("Usuário admin não encontrado", exception1.getMessage());
    assertEquals("Usuário recepcionista não encontrado", exception2.getMessage());
    assertNotEquals(exception1.getMessage(), exception2.getMessage());
  }

  @Test
  void testExceptionCanBeThrown() {
    assertThrows(UsuarioNaoEncontradoException.class, () -> {
      throw new UsuarioNaoEncontradoException("Usuário não existe");
    });
  }

  @Test
  void testExceptionMessageWithLogin() {
    String login = "joao.silva";
    String message = String.format("Usuário com login %s não foi encontrado", login);
    UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(message);

    assertEquals("Usuário com login joao.silva não foi encontrado", exception.getMessage());
    assertTrue(exception.getMessage().contains(login));
  }
}

