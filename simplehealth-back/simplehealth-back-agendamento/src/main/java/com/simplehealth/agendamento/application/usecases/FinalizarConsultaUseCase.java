package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.dtos.FinalizarServicoDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinalizarConsultaUseCase {

  private final ConsultaRepository consultaRepository;

  public ConsultaResponseDTO execute(FinalizarServicoDTO dto) {
    Consulta consulta = consultaRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Consulta não encontrada com ID: " + dto.getId()));

    if (consulta.getStatus() != StatusAgendamentoEnum.ATIVO) {
      throw new IllegalStateException("Apenas consultas ativas podem ser finalizadas");
    }

    if (consulta.getDataHoraInicioExecucao() == null) {
      throw new IllegalStateException("Esta consulta ainda não foi iniciada");
    }

    if (consulta.getDataHoraFimExecucao() != null) {
      throw new IllegalStateException("Esta consulta já foi finalizada");
    }

    consulta.setDataHoraFimExecucao(LocalDateTime.now());
    consulta.setUsuarioFinalizouServicoLogin(dto.getUsuarioLogin());
    consulta.setStatus(StatusAgendamentoEnum.FINALIZADO);
    
    if (dto.getObservacoes() != null && !dto.getObservacoes().isBlank()) {
      String observacoesAtuais = consulta.getObservacoes() != null ? consulta.getObservacoes() + "\n" : "";
      consulta.setObservacoes(observacoesAtuais + dto.getObservacoes());
    }

    Consulta atualizada = consultaRepository.save(consulta);

    return toResponseDTO(atualizada);
  }

  private ConsultaResponseDTO toResponseDTO(Consulta consulta) {
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
