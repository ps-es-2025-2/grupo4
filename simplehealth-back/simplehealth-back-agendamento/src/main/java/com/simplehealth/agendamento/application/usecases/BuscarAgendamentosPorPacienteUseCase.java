package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Agendamento;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarAgendamentosPorPacienteUseCase {

  private final AgendamentoService agendamentoService;

  public List<AgendamentoDTO> execute(String cpf) {
    List<Agendamento> agendamentos = agendamentoService.buscarHistorico(cpf);
    return agendamentos.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
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

