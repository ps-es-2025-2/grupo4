package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.redis.RedisEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CancelarAgendamentoUseCase {

  private final AgendamentoService agendamentoService;
  private final ConsultaService consultaService;
  private final RedisEventPublisher redisEventPublisher;

  public CancelarAgendamentoUseCase(
      ConsultaService consultaService,
      AgendamentoService agendamentoService, RedisEventPublisher redisEventPublisher
  ) {
    this.consultaService = consultaService;
    this.agendamentoService = agendamentoService;
    this.redisEventPublisher = redisEventPublisher;
  }

  public Consulta execute(CancelarAgendamentoDTO dto) throws Exception {
    Consulta consulta = consultaService.buscarPorId(dto.getId())
        .orElseThrow(() -> new Exception("Consulta não encontrada"));

    agendamentoService.validarCancelamento(consulta, dto.getMotivo());

    consulta.setStatus(StatusAgendamentoEnum.CANCELADO);
    consulta.setMotivoCancelamento(dto.getMotivo());
    consulta.setDataCancelamento(dto.getDataHoraCancelamento());
    consulta.setUsuarioCanceladorLogin(dto.getUsuarioLogin());

    Consulta salva = consultaService.salvar(consulta);

    redisEventPublisher.publicar("agendamento.cancelado", salva);

    return salva;
  }

}
