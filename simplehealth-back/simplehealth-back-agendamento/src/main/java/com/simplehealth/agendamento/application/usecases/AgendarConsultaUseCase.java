package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendarConsultaDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.springframework.stereotype.Service;

@Service
public class AgendarConsultaUseCase {

  private final AgendamentoService agendamentoService;
  private final ConsultaService consultaService;

  public AgendarConsultaUseCase(
      AgendamentoService agendamentoService, ConsultaService consultaService) {
    this.agendamentoService = agendamentoService;
    this.consultaService = consultaService;
  }

  public Consulta execute(AgendarConsultaDTO dto) throws Exception {

    agendamentoService.verificarDisponibilidade(
        dto.getMedicoCrm(),
        dto.getDataHoraInicio(),
        dto.getDataHoraFim()
    );

    Consulta consulta = new Consulta();
    consulta.setPacienteCpf(dto.getPacienteCpf());
    consulta.setMedicoCrm(dto.getMedicoCrm());
    consulta.setDataHoraInicio(dto.getDataHoraInicio());
    consulta.setDataHoraFim(dto.getDataHoraFim());
    consulta.setTipoConsulta(dto.getTipoConsulta());
    consulta.setEspecialidade(dto.getEspecialidade());
    consulta.setConvenioNome(dto.getConvenioNome());
    consulta.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);

    Consulta salva = consultaService.salvar(consulta);

    return salva;
  }
}
