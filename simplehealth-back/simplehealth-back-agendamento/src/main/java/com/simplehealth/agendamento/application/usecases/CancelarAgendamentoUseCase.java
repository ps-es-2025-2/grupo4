package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.springframework.stereotype.Service;

@Service
public class CancelarAgendamentoUseCase {

  private final AgendamentoService agendamentoService;
  private final ConsultaService consultaService;

  public CancelarAgendamentoUseCase(
      ConsultaService consultaService,
      AgendamentoService agendamentoService) {
    this.consultaService = consultaService;
    this.agendamentoService = agendamentoService;
  }

  public ConsultaResponseDTO execute(CancelarAgendamentoDTO dto) throws Exception {
    Consulta consulta = consultaService.buscarPorId(dto.getId())
        .orElseThrow(() -> new Exception("Consulta não encontrada"));

    agendamentoService.validarCancelamento(consulta, dto.getMotivo());

    consulta.setStatus(StatusAgendamentoEnum.CANCELADO);
    consulta.setMotivoCancelamento(dto.getMotivo());
    consulta.setDataCancelamento(dto.getDataHoraCancelamento());
    consulta.setUsuarioCanceladorLogin(dto.getUsuarioLogin());

    Consulta salva = consultaService.salvar(consulta);

    return toResponseDTO(salva);
  }

  private ConsultaResponseDTO toResponseDTO(Consulta consulta) {
    return ConsultaResponseDTO.builder()
        .id(consulta.getId())
        .dataHoraInicio(consulta.getDataHoraInicio())
        .dataHoraFim(consulta.getDataHoraFim())
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
