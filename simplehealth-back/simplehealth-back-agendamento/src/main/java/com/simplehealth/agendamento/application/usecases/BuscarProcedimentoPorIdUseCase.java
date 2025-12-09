package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.ProcedimentoResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarProcedimentoPorIdUseCase {

  private final ProcedimentoRepository procedimentoRepository;

  public ProcedimentoResponseDTO execute(String id) {
    Procedimento procedimento = procedimentoRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Procedimento não encontrado com ID: " + id));

    return toResponseDTO(procedimento);
  }

  private ProcedimentoResponseDTO toResponseDTO(Procedimento procedimento) {
    return ProcedimentoResponseDTO.builder()
        .id(procedimento.getId())
        .dataHoraAgendamento(procedimento.getDataHoraAgendamento())
        .dataHoraInicioPrevista(procedimento.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(procedimento.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(procedimento.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(procedimento.getDataHoraFimExecucao())
        .isEncaixe(procedimento.getIsEncaixe())
        .modalidade(procedimento.getModalidade())
        .motivoEncaixe(procedimento.getMotivoEncaixe())
        .observacoes(procedimento.getObservacoes())
        .status(procedimento.getStatus())
        .motivoCancelamento(procedimento.getMotivoCancelamento())
        .dataCancelamento(procedimento.getDataCancelamento())
        .pacienteCpf(procedimento.getPacienteCpf())
        .medicoCrm(procedimento.getMedicoCrm())
        .convenioNome(procedimento.getConvenioNome())
        .usuarioCriadorLogin(procedimento.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(procedimento.getUsuarioCanceladorLogin())
        .descricaoProcedimento(procedimento.getDescricaoProcedimento())
        .salaEquipamentoNecessario(procedimento.getSalaEquipamentoNecessario())
        .nivelRisco(procedimento.getNivelRisco())
        .build();
  }
}

