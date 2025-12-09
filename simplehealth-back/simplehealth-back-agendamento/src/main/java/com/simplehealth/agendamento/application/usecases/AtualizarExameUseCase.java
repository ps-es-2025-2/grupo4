package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AtualizarExameDTO;
import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarExameUseCase {

  private final ExameRepository exameRepository;
  private final AgendamentoService agendamentoService;

  public ExameResponseDTO execute(AtualizarExameDTO dto) throws Exception {
    if (dto.getId() == null || dto.getId().isBlank()) {
      throw new IllegalArgumentException("O ID do exame é obrigatório");
    }

    Exame exame = exameRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Exame não encontrado com ID: " + dto.getId()));

    if (exame.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("Não é possível atualizar um exame cancelado");
    }

    if (exame.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Não é possível atualizar um exame que já foi iniciado");
    }

    if (dto.getDataHoraInicio() != null && dto.getDataHoraFim() != null) {
      if (!dto.getDataHoraInicio().equals(exame.getDataHoraInicioPrevista())
          || !dto.getDataHoraFim().equals(exame.getDataHoraFimPrevista())) {
        agendamentoService.verificarDisponibilidade(
            exame.getMedicoCrm(),
            dto.getDataHoraInicio(),
            dto.getDataHoraFim());
      }
      exame.setDataHoraInicioPrevista(dto.getDataHoraInicio());
      exame.setDataHoraFimPrevista(dto.getDataHoraFim());
    }

    if (dto.getNomeExame() != null) {
      exame.setNomeExame(dto.getNomeExame());
    }
    if (dto.getRequerPreparo() != null) {
      exame.setRequerPreparo(dto.getRequerPreparo());
    }
    if (dto.getInstrucoesPreparo() != null) {
      exame.setInstrucoesPreparo(dto.getInstrucoesPreparo());
    }
    if (dto.getModalidade() != null) {
      exame.setModalidade(dto.getModalidade());
    }
    if (dto.getObservacoes() != null) {
      exame.setObservacoes(dto.getObservacoes());
    }
    if (dto.getConvenioNome() != null) {
      exame.setConvenioNome(dto.getConvenioNome());
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

