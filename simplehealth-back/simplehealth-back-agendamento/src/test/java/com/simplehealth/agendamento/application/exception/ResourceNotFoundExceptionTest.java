package com.simplehealth.agendamento.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

  @Test
  void testExceptionMessage() {
    String message = "Recurso não encontrado";
    ResourceNotFoundException exception = new ResourceNotFoundException(message);

    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionIsInstanceOfRuntimeException() {
    ResourceNotFoundException exception = new ResourceNotFoundException("Teste");

    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  void testExceptionWithDifferentMessages() {
    ResourceNotFoundException exception1 = new ResourceNotFoundException("Agendamento não encontrado");
    ResourceNotFoundException exception2 = new ResourceNotFoundException("Consulta não encontrada");

    assertEquals("Agendamento não encontrado", exception1.getMessage());
    assertEquals("Consulta não encontrada", exception2.getMessage());
    assertNotEquals(exception1.getMessage(), exception2.getMessage());
  }

  @Test
  void testExceptionCanBeThrown() {
    assertThrows(ResourceNotFoundException.class, () -> {
      throw new ResourceNotFoundException("Recurso não existe");
    });
  }

  @Test
  void testExceptionMessageWithId() {
    String id = "agend123";
    String message = String.format("Recurso com ID %s não foi encontrado", id);
    ResourceNotFoundException exception = new ResourceNotFoundException(message);

    assertEquals("Recurso com ID agend123 não foi encontrado", exception.getMessage());
    assertTrue(exception.getMessage().contains(id));
  }

  @Test
  void testExceptionWithDetailedMessage() {
    ResourceNotFoundException exception = new ResourceNotFoundException(
        "O agendamento solicitado não foi encontrado no sistema"
    );

    assertTrue(exception.getMessage().contains("agendamento"));
    assertTrue(exception.getMessage().contains("não foi encontrado"));
    assertTrue(exception.getMessage().contains("sistema"));
  }

  @Test
  void testExceptionInheritance() {
    ResourceNotFoundException exception = new ResourceNotFoundException("Teste");

    assertInstanceOf(RuntimeException.class, exception);
    assertInstanceOf(Exception.class, exception);
  }

  @Test
  void testMultipleResourceTypes() {
    ResourceNotFoundException exception1 = new ResourceNotFoundException("Consulta ID 123 não encontrada");
    ResourceNotFoundException exception2 = new ResourceNotFoundException("Exame ID 456 não encontrado");
    ResourceNotFoundException exception3 = new ResourceNotFoundException("Procedimento ID 789 não encontrado");

    assertTrue(exception1.getMessage().contains("Consulta"));
    assertTrue(exception2.getMessage().contains("Exame"));
    assertTrue(exception3.getMessage().contains("Procedimento"));
  }
}

