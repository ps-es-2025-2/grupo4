package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.AtualizarAgendamentoDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarAgendamentoUseCase {

  private final AgendamentoService agendamentoService;

  public AgendamentoDTO execute(AtualizarAgendamentoDTO dto) throws Exception {
    if (dto.getId() == null || dto.getId().isBlank()) {
      throw new IllegalArgumentException("O ID do agendamento é obrigatório");
    }

    Agendamento agendamento = agendamentoService.buscarPorId(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Agendamento não encontrado com ID: " + dto.getId()));

    // Validar se o agendamento não está cancelado
    if (agendamento.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("Não é possível atualizar um agendamento cancelado");
    }

    // Validar se o serviço já foi iniciado
    if (agendamento.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Não é possível atualizar um agendamento que já foi iniciado");
    }

    // Se houver mudança de horário previsto, verificar disponibilidade
    if (dto.getDataHoraInicio() != null && dto.getDataHoraFim() != null) {
      if (!dto.getDataHoraInicio().equals(agendamento.getDataHoraInicioPrevista())
          || !dto.getDataHoraFim().equals(agendamento.getDataHoraFimPrevista())) {
        agendamentoService.verificarDisponibilidade(
            agendamento.getMedicoCrm(),
            dto.getDataHoraInicio(),
            dto.getDataHoraFim());
      }
      agendamento.setDataHoraInicioPrevista(dto.getDataHoraInicio());
      agendamento.setDataHoraFimPrevista(dto.getDataHoraFim());
    }

    // Atualizar campos permitidos
    if (dto.getModalidade() != null) {
      agendamento.setModalidade(dto.getModalidade());
    }
    if (dto.getObservacoes() != null) {
      agendamento.setObservacoes(dto.getObservacoes());
    }
    if (dto.getConvenioNome() != null) {
      agendamento.setConvenioNome(dto.getConvenioNome());
    }

    Agendamento atualizado = agendamentoService.salvar(agendamento);

    return toDTO(atualizado);
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

