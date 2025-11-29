package com.simplehealth.agendamento.infrastructure.redis.publishers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisEventPublisherTest {

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @InjectMocks
  private RedisEventPublisher redisEventPublisher;

  private Object eventoTeste;

  @BeforeEach
  void setUp() {
    eventoTeste = new Object() {
      public String mensagem = "Teste de evento";
    };
  }

  @Test
  void testPublicarEvento() {
    String canal = "teste.canal";

    redisEventPublisher.publicar(canal, eventoTeste);

    verify(redisTemplate, times(1)).convertAndSend(eq(canal), eq(eventoTeste));
  }

  @Test
  void testPublicarEventoComCanalDiferente() {
    String canal = "outro.canal";

    redisEventPublisher.publicar(canal, eventoTeste);

    verify(redisTemplate, times(1)).convertAndSend(eq(canal), any());
  }

  @Test
  void testPublicarEventoNulo() {
    String canal = "teste.canal";

    redisEventPublisher.publicar(canal, null);

    verify(redisTemplate, times(1)).convertAndSend(eq(canal), eq(null));
  }

  @Test
  void testPublicarMultiplosEventos() {
    String canal1 = "canal1";
    String canal2 = "canal2";
    Object evento1 = new Object();
    Object evento2 = new Object();

    redisEventPublisher.publicar(canal1, evento1);
    redisEventPublisher.publicar(canal2, evento2);

    verify(redisTemplate, times(1)).convertAndSend(eq(canal1), eq(evento1));
    verify(redisTemplate, times(1)).convertAndSend(eq(canal2), eq(evento2));
    verify(redisTemplate, times(2)).convertAndSend(anyString(), any());
  }
}

