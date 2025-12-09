package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarConsultaPorIdUseCase {

  private final ConsultaRepository consultaRepository;

  public ConsultaResponseDTO execute(String id) {
    Consulta consulta = consultaRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Consulta não encontrada com ID: " + id));

    return toDTO(consulta);
  }

  private ConsultaResponseDTO toDTO(Consulta consulta) {
    return ConsultaResponseDTO.builder()
        .id(consulta.getId())
        .dataHoraAgendamento(consulta.getDataHoraAgendamento())
        .dataHoraInicioPrevista(consulta.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(consulta.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(consulta.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(consulta.getDataHoraFimExecucao())
        .isEncaixe(consulta.getIsEncaixe())
        .modalidade(consulta.getModalidade())
        .motivoEncaixe(consulta.getMotivoEncaixe())
        .observacoes(consulta.getObservacoes())
        .status(consulta.getStatus())
        .motivoCancelamento(consulta.getMotivoCancelamento())
        .dataCancelamento(consulta.getDataCancelamento())
        .pacienteCpf(consulta.getPacienteCpf())
        .medicoCrm(consulta.getMedicoCrm())
        .convenioNome(consulta.getConvenioNome())
        .usuarioCriadorLogin(consulta.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(consulta.getUsuarioCanceladorLogin())
        .especialidade(consulta.getEspecialidade())
        .tipoConsulta(consulta.getTipoConsulta())
        .build();
  }
}
