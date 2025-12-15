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

    LocalDateTime novaDataInicio = dto.getDataHoraInicio() != null ? dto.getDataHoraInicio() 
        : (dto.getDataHoraInicioPrevista() != null ? dto.getDataHoraInicioPrevista() : exame.getDataHoraInicioPrevista());
    
    LocalDateTime novaDataFim = dto.getDataHoraFim() != null ? dto.getDataHoraFim() 
        : (dto.getDataHoraFimPrevista() != null ? dto.getDataHoraFimPrevista() : exame.getDataHoraFimPrevista());
        
    String novoMedicoCrm = dto.getMedicoCrm() != null ? dto.getMedicoCrm() : exame.getMedicoCrm();

    if (novaDataInicio.isAfter(novaDataFim)) {
      throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
    }

    boolean datesChanged = !novaDataInicio.equals(exame.getDataHoraInicioPrevista()) 
                        || !novaDataFim.equals(exame.getDataHoraFimPrevista());
    boolean doctorChanged = !novoMedicoCrm.equals(exame.getMedicoCrm());

    if (datesChanged || doctorChanged) {
      agendamentoService.verificarDisponibilidade(
          novoMedicoCrm,
          novaDataInicio,
          novaDataFim);
      
      exame.setDataHoraInicioPrevista(novaDataInicio);
      exame.setDataHoraFimPrevista(novaDataFim);
      exame.setMedicoCrm(novoMedicoCrm);
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
    if (dto.getPacienteCpf() != null) {
      exame.setPacienteCpf(dto.getPacienteCpf());
    }
    if (dto.getIsEncaixe() != null) {
      exame.setIsEncaixe(dto.getIsEncaixe());
    }
    if (dto.getMotivoEncaixe() != null) {
      exame.setMotivoEncaixe(dto.getMotivoEncaixe());
    }
    if (dto.getStatus() != null) {
      exame.setStatus(dto.getStatus());
    }
    if (dto.getMotivoCancelamento() != null) {
      exame.setMotivoCancelamento(dto.getMotivoCancelamento());
    }
    if (dto.getDataCancelamento() != null) {
      exame.setDataCancelamento(dto.getDataCancelamento());
    }
    if (dto.getDataHoraInicioExecucao() != null) {
      exame.setDataHoraInicioExecucao(dto.getDataHoraInicioExecucao());
    }
    if (dto.getDataHoraFimExecucao() != null) {
      exame.setDataHoraFimExecucao(dto.getDataHoraFimExecucao());
    }
    if (dto.getUsuarioCriadorLogin() != null) {
      exame.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    }
    if (dto.getUsuarioCanceladorLogin() != null) {
      exame.setUsuarioCanceladorLogin(dto.getUsuarioCanceladorLogin());
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

