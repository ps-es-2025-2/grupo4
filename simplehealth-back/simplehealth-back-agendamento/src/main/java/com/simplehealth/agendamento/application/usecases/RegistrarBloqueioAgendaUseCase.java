package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.services.BloqueioAgendaService;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import org.springframework.stereotype.Service;

@Service
public class RegistrarBloqueioAgendaUseCase {

  private final BloqueioAgendaService bloqueioAgendaService;

  public RegistrarBloqueioAgendaUseCase(
      BloqueioAgendaService bloqueioAgendaService
  ) {
    this.bloqueioAgendaService = bloqueioAgendaService;
  }

  public BloqueioAgenda registrar(BloqueioAgendaDTO dto) {
    BloqueioAgenda bloqueio = new BloqueioAgenda();
    bloqueio.setDataInicio(dto.getDataInicio());
    bloqueio.setDataFim(dto.getDataFim());
    bloqueio.setMotivo(dto.getMotivo());
    bloqueio.setAntecedenciaMinima(dto.getAntecedenciaMinima());
    bloqueio.setMedicoCrm(dto.getMedicoCrm());
    bloqueio.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());

    BloqueioAgenda salvo = bloqueioAgendaService.salvar(bloqueio);

    return salvo;
  }
}
