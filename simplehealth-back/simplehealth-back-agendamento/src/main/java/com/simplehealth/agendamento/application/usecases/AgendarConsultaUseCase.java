package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendarConsultaDTO;
import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AgendarConsultaUseCase {

  private final AgendamentoService agendamentoService;
  private final ConsultaService consultaService;

  public AgendarConsultaUseCase(
      AgendamentoService agendamentoService, ConsultaService consultaService) {
    this.agendamentoService = agendamentoService;
    this.consultaService = consultaService;
  }

  public ConsultaResponseDTO execute(AgendarConsultaDTO dto) throws Exception {
    // Verifica disponibilidade baseado nas datas previstas
    if (dto.getDataHoraInicioPrevista() != null && dto.getDataHoraFimPrevista() != null) {
      agendamentoService.verificarDisponibilidade(
          dto.getMedicoCrm(),
          dto.getDataHoraInicioPrevista(),
          dto.getDataHoraFimPrevista());
    }

    Consulta consulta = new Consulta();
    consulta.setPacienteCpf(dto.getPacienteCpf());
    consulta.setMedicoCrm(dto.getMedicoCrm());
    consulta.setDataHoraAgendamento(LocalDateTime.now());
    consulta.setDataHoraInicioPrevista(dto.getDataHoraInicioPrevista());
    consulta.setDataHoraFimPrevista(dto.getDataHoraFimPrevista());
    // dataHoraInicioExecucao e dataHoraFimExecucao serão definidos ao iniciar/finalizar serviço
    consulta.setTipoConsulta(dto.getTipoConsulta());
    consulta.setEspecialidade(dto.getEspecialidade());
    consulta.setConvenioNome(dto.getConvenioNome());
    consulta.setModalidade(dto.getModalidade());
    consulta.setObservacoes(dto.getObservacoes());
    consulta.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);

    Consulta salva = consultaService.salvar(consulta);

    return toResponseDTO(salva);
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