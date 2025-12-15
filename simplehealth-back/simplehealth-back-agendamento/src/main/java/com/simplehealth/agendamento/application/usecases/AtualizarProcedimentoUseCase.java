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

    LocalDateTime novaDataInicio = dto.getDataHoraInicio() != null ? dto.getDataHoraInicio() 
        : (dto.getDataHoraInicioPrevista() != null ? dto.getDataHoraInicioPrevista() : procedimento.getDataHoraInicioPrevista());
    
    LocalDateTime novaDataFim = dto.getDataHoraFim() != null ? dto.getDataHoraFim() 
        : (dto.getDataHoraFimPrevista() != null ? dto.getDataHoraFimPrevista() : procedimento.getDataHoraFimPrevista());
        
    String novoMedicoCrm = dto.getMedicoCrm() != null ? dto.getMedicoCrm() : procedimento.getMedicoCrm();

    if (novaDataInicio.isAfter(novaDataFim)) {
      throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
    }

    boolean datesChanged = !novaDataInicio.equals(procedimento.getDataHoraInicioPrevista()) 
                        || !novaDataFim.equals(procedimento.getDataHoraFimPrevista());
    boolean doctorChanged = !novoMedicoCrm.equals(procedimento.getMedicoCrm());

    if (datesChanged || doctorChanged) {
      agendamentoService.verificarDisponibilidade(
          novoMedicoCrm,
          novaDataInicio,
          novaDataFim);
      
      procedimento.setDataHoraInicioPrevista(novaDataInicio);
      procedimento.setDataHoraFimPrevista(novaDataFim);
      procedimento.setMedicoCrm(novoMedicoCrm);
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
    if (dto.getPacienteCpf() != null) {
      procedimento.setPacienteCpf(dto.getPacienteCpf());
    }
    if (dto.getIsEncaixe() != null) {
      procedimento.setIsEncaixe(dto.getIsEncaixe());
    }
    if (dto.getMotivoEncaixe() != null) {
      procedimento.setMotivoEncaixe(dto.getMotivoEncaixe());
    }
    if (dto.getStatus() != null) {
      procedimento.setStatus(dto.getStatus());
    }
    if (dto.getMotivoCancelamento() != null) {
      procedimento.setMotivoCancelamento(dto.getMotivoCancelamento());
    }
    if (dto.getDataCancelamento() != null) {
      procedimento.setDataCancelamento(dto.getDataCancelamento());
    }
    if (dto.getDataHoraInicioExecucao() != null) {
      procedimento.setDataHoraInicioExecucao(dto.getDataHoraInicioExecucao());
    }
    if (dto.getDataHoraFimExecucao() != null) {
      procedimento.setDataHoraFimExecucao(dto.getDataHoraFimExecucao());
    }
    if (dto.getUsuarioCriadorLogin() != null) {
      procedimento.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    }
    if (dto.getUsuarioCanceladorLogin() != null) {
      procedimento.setUsuarioCanceladorLogin(dto.getUsuarioCanceladorLogin());
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

