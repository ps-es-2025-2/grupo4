package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.EncaixeDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.redis.publishers.RedisEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SolicitarEncaixeUseCase {

  private final ConsultaService consultaService;
  private final AgendamentoService agendamentoService;
  private final RedisEventPublisher redisPublisher;

  public SolicitarEncaixeUseCase(
      ConsultaService consultaService,
      AgendamentoService agendamentoService,
      RedisEventPublisher redisPublisher
  ) {
    this.consultaService = consultaService;
    this.agendamentoService = agendamentoService;
    this.redisPublisher = redisPublisher;
  }

  public Consulta execute(EncaixeDTO dto) throws Exception {

    agendamentoService.verificarDisponibilidade(
        dto.getMedicoCrm(),
        dto.getDataHoraInicio(),
        dto.getDataHoraFim()
    );

    Consulta consulta = new Consulta();
    consulta.setPacienteCpf(dto.getPacienteCpf());
    consulta.setMedicoCrm(dto.getMedicoCrm());
    consulta.setDataHoraInicio(dto.getDataHoraInicio());
    consulta.setDataHoraFim(dto.getDataHoraFim());
    consulta.setIsEncaixe(true);
    consulta.setMotivoEncaixe(dto.getMotivoEncaixe());
    consulta.setObservacoes(dto.getObservacoes());
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
    consulta.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());

    Consulta salvo = consultaService.salvar(consulta);

    redisPublisher.publicar("encaixe.solicitado", salvo);

    return salvo;
  }
}
