package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.dtos.EncaixeDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SolicitarEncaixeUseCase {

  private final ConsultaService consultaService;
  private final AgendamentoService agendamentoService;

  public SolicitarEncaixeUseCase(
      ConsultaService consultaService,
      AgendamentoService agendamentoService) {
    this.consultaService = consultaService;
    this.agendamentoService = agendamentoService;
  }

  public ConsultaResponseDTO execute(EncaixeDTO dto) throws Exception {

    agendamentoService.verificarDisponibilidade(
        dto.getMedicoCrm(),
        dto.getDataHoraInicio(),
        dto.getDataHoraFim());

    Consulta consulta = new Consulta();
    consulta.setPacienteCpf(dto.getPacienteCpf());
    consulta.setMedicoCrm(dto.getMedicoCrm());
    consulta.setDataHoraAgendamento(LocalDateTime.now());
    consulta.setDataHoraInicioPrevista(dto.getDataHoraInicio());
    consulta.setDataHoraFimPrevista(dto.getDataHoraFim());
    consulta.setIsEncaixe(true);
    consulta.setMotivoEncaixe(dto.getMotivoEncaixe());
    consulta.setObservacoes(dto.getObservacoes());
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
    consulta.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());

    if (dto.getTipoConsulta() != null) {
      consulta.setTipoConsulta(dto.getTipoConsulta());
    }
    if (dto.getEspecialidade() != null) {
      consulta.setEspecialidade(dto.getEspecialidade());
    }
    if (dto.getConvenioNome() != null) {
      consulta.setConvenioNome(dto.getConvenioNome());
    }
    if (dto.getModalidade() != null) {
      consulta.setModalidade(dto.getModalidade());
    }

    Consulta salvo = consultaService.salvar(consulta);

    return toResponseDTO(salvo);
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
