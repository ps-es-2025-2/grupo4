package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AtualizarAgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarConsultaUseCase {

  private final ConsultaRepository consultaRepository;
  private final AgendamentoService agendamentoService;

  public ConsultaResponseDTO execute(AtualizarAgendamentoDTO dto) throws Exception {
    if (dto.getId() == null || dto.getId().isBlank()) {
      throw new IllegalArgumentException("O ID da consulta é obrigatório");
    }

    Consulta consulta = consultaRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Agendamento não encontrado com ID: " + dto.getId()));

    // Validar se o agendamento não está cancelado
    if (consulta.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("Não é possível atualizar um agendamento cancelado");
    }

    // Validar se o serviço já foi iniciado
    if (consulta.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Não é possível atualizar um agendamento que já foi iniciado");
    }

    // Se houver mudança de horário previsto, verificar disponibilidade
    if (dto.getDataHoraInicio() != null && dto.getDataHoraFim() != null) {
      if (!dto.getDataHoraInicio().equals(consulta.getDataHoraInicioPrevista())
          || !dto.getDataHoraFim().equals(consulta.getDataHoraFimPrevista())) {
        agendamentoService.verificarDisponibilidade(
            consulta.getMedicoCrm(),
            dto.getDataHoraInicio(),
            dto.getDataHoraFim());
      }
      consulta.setDataHoraInicioPrevista(dto.getDataHoraInicio());
      consulta.setDataHoraFimPrevista(dto.getDataHoraFim());
    }

    // Atualizar campos permitidos
    if (dto.getModalidade() != null) {
      consulta.setModalidade(dto.getModalidade());
    }
    if (dto.getObservacoes() != null) {
      consulta.setObservacoes(dto.getObservacoes());
    }
    if (dto.getConvenioNome() != null) {
      consulta.setConvenioNome(dto.getConvenioNome());
    }
    
    // Atualizar campos específicos de Consulta (se enviados no DTO)
    if (dto.getEspecialidade() != null) {
      consulta.setEspecialidade(dto.getEspecialidade());
    }
    if (dto.getTipoConsulta() != null) {
      consulta.setTipoConsulta(dto.getTipoConsulta());
    }
    
    // Atualizar campos de encaixe
    if (dto.getIsEncaixe() != null) {
      consulta.setIsEncaixe(dto.getIsEncaixe());
      // Se não é mais encaixe, limpa o motivo
      if (!dto.getIsEncaixe()) {
        consulta.setMotivoEncaixe(null);
      }
    }
    // Atualiza motivo de encaixe se for encaixe e tiver motivo informado
    if (consulta.getIsEncaixe() != null && consulta.getIsEncaixe() && dto.getMotivoEncaixe() != null) {
      consulta.setMotivoEncaixe(dto.getMotivoEncaixe());
    }

    Consulta atualizada = consultaRepository.save(consulta);

    return toDTO(atualizada);
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
