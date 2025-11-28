package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.EncaixeDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.springframework.stereotype.Service;

@Service
public class SolicitarEncaixeUseCase {

  private final ConsultaService consultaService;
  private final AgendamentoService agendamentoService;

  public SolicitarEncaixeUseCase(
      ConsultaService consultaService,
      AgendamentoService agendamentoService
  ) {
    this.consultaService = consultaService;
    this.agendamentoService = agendamentoService;
  }

  public Consulta execute(EncaixeDTO dto) throws Exception {

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
    consulta.setIsEncaixe(true);
    consulta.setMotivoEncaixe(dto.getMotivoEncaixe());
    consulta.setObservacoes(dto.getObservacoes());
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
    consulta.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());

    Consulta salvo = consultaService.salvar(consulta);

    return salvo;
  }
}
