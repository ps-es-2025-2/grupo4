package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarExamePorIdUseCase {

  private final ExameRepository exameRepository;

  public ExameResponseDTO execute(String id) {
    Exame exame = exameRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Exame não encontrado com ID: " + id));

    return toResponseDTO(exame);
  }

  private ExameResponseDTO toResponseDTO(Exame exame) {
    return ExameResponseDTO.builder()
        .id(exame.getId())
        .dataHoraAgendamento(exame.getDataHoraAgendamento())
        .dataHoraInicioPrevista(exame.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(exame.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(exame.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(exame.getDataHoraFimExecucao())
        .isEncaixe(exame.getIsEncaixe())
        .modalidade(exame.getModalidade())
        .motivoEncaixe(exame.getMotivoEncaixe())
        .observacoes(exame.getObservacoes())
        .status(exame.getStatus())
        .motivoCancelamento(exame.getMotivoCancelamento())
        .dataCancelamento(exame.getDataCancelamento())
        .pacienteCpf(exame.getPacienteCpf())
        .medicoCrm(exame.getMedicoCrm())
        .convenioNome(exame.getConvenioNome())
        .usuarioCriadorLogin(exame.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(exame.getUsuarioCanceladorLogin())
        .nomeExame(exame.getNomeExame())
        .requerPreparo(exame.getRequerPreparo())
        .instrucoesPreparo(exame.getInstrucoesPreparo())
        .build();
  }
}

