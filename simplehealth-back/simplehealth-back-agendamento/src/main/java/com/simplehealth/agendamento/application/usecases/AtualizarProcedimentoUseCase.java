package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AtualizarProcedimentoDTO;
import com.simplehealth.agendamento.application.dtos.ProcedimentoResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarProcedimentoUseCase {

  private final ProcedimentoRepository procedimentoRepository;
  private final AgendamentoService agendamentoService;

  public ProcedimentoResponseDTO execute(AtualizarProcedimentoDTO dto) throws Exception {
    if (dto.getId() == null || dto.getId().isBlank()) {
      throw new IllegalArgumentException("O ID do procedimento é obrigatório");
    }

    Procedimento procedimento = procedimentoRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Procedimento não encontrado com ID: " + dto.getId()));

    if (procedimento.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("Não é possível atualizar um procedimento cancelado");
    }

    if (procedimento.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Não é possível atualizar um procedimento que já foi iniciado");
    }

    if (dto.getDataHoraInicio() != null && dto.getDataHoraFim() != null) {
      if (!dto.getDataHoraInicio().equals(procedimento.getDataHoraInicioPrevista())
          || !dto.getDataHoraFim().equals(procedimento.getDataHoraFimPrevista())) {
        agendamentoService.verificarDisponibilidade(
            procedimento.getMedicoCrm(),
            dto.getDataHoraInicio(),
            dto.getDataHoraFim());
      }
      procedimento.setDataHoraInicioPrevista(dto.getDataHoraInicio());
      procedimento.setDataHoraFimPrevista(dto.getDataHoraFim());
    }

    if (dto.getDescricaoProcedimento() != null) {
      procedimento.setDescricaoProcedimento(dto.getDescricaoProcedimento());
    }
    if (dto.getSalaEquipamentoNecessario() != null) {
      procedimento.setSalaEquipamentoNecessario(dto.getSalaEquipamentoNecessario());
    }
    if (dto.getNivelRisco() != null) {
      procedimento.setNivelRisco(dto.getNivelRisco());
    }
    if (dto.getModalidade() != null) {
      procedimento.setModalidade(dto.getModalidade());
    }
    if (dto.getObservacoes() != null) {
      procedimento.setObservacoes(dto.getObservacoes());
    }
    if (dto.getConvenioNome() != null) {
      procedimento.setConvenioNome(dto.getConvenioNome());
    }

    Procedimento atualizado = procedimentoRepository.save(procedimento);

    return toResponseDTO(atualizado);
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

