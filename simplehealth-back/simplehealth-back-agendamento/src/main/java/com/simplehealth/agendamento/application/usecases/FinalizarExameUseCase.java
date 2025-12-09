package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import com.simplehealth.agendamento.application.dtos.FinalizarServicoDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinalizarExameUseCase {

  private final ExameRepository exameRepository;

  public ExameResponseDTO execute(FinalizarServicoDTO dto) {
    Exame exame = exameRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Exame não encontrado com ID: " + dto.getId()));

    if (exame.getStatus() != StatusAgendamentoEnum.ATIVO) {
      throw new IllegalStateException("Apenas exames ativos podem ser finalizados");
    }

    if (exame.getDataHoraInicioExecucao() == null) {
      throw new IllegalStateException("Este exame ainda não foi iniciado");
    }

    if (exame.getDataHoraFimExecucao() != null) {
      throw new IllegalStateException("Este exame já foi finalizado");
    }

    exame.setDataHoraFimExecucao(LocalDateTime.now());
    exame.setUsuarioFinalizouServicoLogin(dto.getUsuarioLogin());
    
    if (dto.getObservacoes() != null && !dto.getObservacoes().isBlank()) {
      String observacoesAtuais = exame.getObservacoes() != null ? exame.getObservacoes() + "\n" : "";
      exame.setObservacoes(observacoesAtuais + dto.getObservacoes());
    }

    Exame atualizado = exameRepository.save(exame);

    return toResponseDTO(atualizado);
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
