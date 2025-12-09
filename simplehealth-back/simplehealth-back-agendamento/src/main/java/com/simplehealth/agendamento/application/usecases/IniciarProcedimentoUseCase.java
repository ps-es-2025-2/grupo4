package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.IniciarServicoDTO;
import com.simplehealth.agendamento.application.dtos.ProcedimentoResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IniciarProcedimentoUseCase {

  private final ProcedimentoRepository procedimentoRepository;

  public ProcedimentoResponseDTO execute(IniciarServicoDTO dto) {
    Procedimento procedimento = procedimentoRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Procedimento não encontrado com ID: " + dto.getId()));

    if (procedimento.getStatus() != StatusAgendamentoEnum.ATIVO) {
      throw new IllegalStateException("Apenas procedimentos ativos podem ser iniciados");
    }

    if (procedimento.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Este procedimento já foi iniciado");
    }

    procedimento.setDataHoraInicioExecucao(LocalDateTime.now());
    procedimento.setUsuarioIniciouServicoLogin(dto.getUsuarioLogin());

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
