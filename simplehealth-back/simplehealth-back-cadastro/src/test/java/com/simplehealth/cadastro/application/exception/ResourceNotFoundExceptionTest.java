package com.simplehealth.cadastro.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

  @Test
  void testExceptionMessage() {
    String message = "Recurso não encontrado";
    ResourceNotFoundException exception = new ResourceNotFoundException(message);

    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionIsRuntimeException() {
    ResourceNotFoundException exception = new ResourceNotFoundException("Teste");

    assertTrue(exception instanceof RuntimeException);
  }
}

