package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Agendamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarAgendamentoPorIdUseCase {

  private final AgendamentoService agendamentoService;

  public AgendamentoDTO execute(String id) {
    Agendamento agendamento = agendamentoService.buscarPorId(id)
        .orElseThrow(() -> new AgendamentoException("Agendamento não encontrado com ID: " + id));

    return toDTO(agendamento);
  }

  private AgendamentoDTO toDTO(Agendamento agendamento) {
    return AgendamentoDTO.builder()
        .id(agendamento.getId())
        .dataHoraInicio(agendamento.getDataHoraInicioPrevista())
        .dataHoraFim(agendamento.getDataHoraFimPrevista())
        .isEncaixe(agendamento.getIsEncaixe())
        .modalidade(agendamento.getModalidade() != null ? agendamento.getModalidade().name() : null)
        .motivoEncaixe(agendamento.getMotivoEncaixe())
        .observacoes(agendamento.getObservacoes())
        .status(agendamento.getStatus() != null ? agendamento.getStatus().name() : null)
        .motivoCancelamento(agendamento.getMotivoCancelamento())
        .dataCancelamento(agendamento.getDataCancelamento())
        .pacienteCpf(agendamento.getPacienteCpf())
        .medicoCrm(agendamento.getMedicoCrm())
        .convenioNome(agendamento.getConvenioNome())
        .usuarioCriadorLogin(agendamento.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(agendamento.getUsuarioCanceladorLogin())
        .build();
  }
}

