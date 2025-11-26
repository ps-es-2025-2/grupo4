package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.services.BloqueioAgendaService;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.redis.publishers.RedisEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RegistrarBloqueioAgendaUseCase {

  private final BloqueioAgendaService bloqueioAgendaService;
  private final RedisEventPublisher redisPublisher;

  public RegistrarBloqueioAgendaUseCase(
      BloqueioAgendaService bloqueioAgendaService,
      RedisEventPublisher redisPublisher
  ) {
    this.bloqueioAgendaService = bloqueioAgendaService;
    this.redisPublisher = redisPublisher;
  }

  public BloqueioAgenda registrar(BloqueioAgendaDTO dto) {
    BloqueioAgenda bloqueio = new BloqueioAgenda();
    bloqueio.setDataInicio(dto.getDataInicio());
    bloqueio.setDataFim(dto.getDataFim());
    bloqueio.setMotivo(dto.getMotivo());
    bloqueio.setAntecedenciaMinima(dto.getAntecedenciaMinima());
    bloqueio.setMedicoCrm(dto.getMedicoCrm());
    bloqueio.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());

    BloqueioAgenda salvo = bloqueioAgendaService.salvar(bloqueio);

    redisPublisher.publicar("bloqueio.criado", salvo);

    return salvo;
  }
}
