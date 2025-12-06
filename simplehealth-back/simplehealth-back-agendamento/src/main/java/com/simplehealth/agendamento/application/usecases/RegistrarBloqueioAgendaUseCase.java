package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.dtos.BloqueioAgendaResponseDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.BloqueioAgendaService;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import org.springframework.stereotype.Service;

@Service
public class RegistrarBloqueioAgendaUseCase {

  private final BloqueioAgendaService bloqueioAgendaService;
  private final AgendamentoService agendamentoService;

  public RegistrarBloqueioAgendaUseCase(
      BloqueioAgendaService bloqueioAgendaService,
      AgendamentoService agendamentoService) {
    this.bloqueioAgendaService = bloqueioAgendaService;
    this.agendamentoService = agendamentoService;
  }

  public BloqueioAgendaResponseDTO registrar(BloqueioAgendaDTO dto) throws Exception {
    agendamentoService.verificarAgendamentosAtivosNoPeriodo(
        dto.getMedicoCrm(),
        dto.getDataInicio(),
        dto.getDataFim());

    BloqueioAgenda bloqueio = new BloqueioAgenda();
    bloqueio.setDataInicio(dto.getDataInicio());
    bloqueio.setDataFim(dto.getDataFim());
    bloqueio.setMotivo(dto.getMotivo());
    bloqueio.setAntecedenciaMinima(dto.getAntecedenciaMinima());
    bloqueio.setMedicoCrm(dto.getMedicoCrm());
    bloqueio.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());

    BloqueioAgenda salvo = bloqueioAgendaService.salvar(bloqueio);

    return toResponseDTO(salvo);
  }

  private BloqueioAgendaResponseDTO toResponseDTO(BloqueioAgenda bloqueio) {
    return BloqueioAgendaResponseDTO.builder()
        .id(bloqueio.getId())
        .dataInicio(bloqueio.getDataInicio())
        .dataFim(bloqueio.getDataFim())
        .motivo(bloqueio.getMotivo())
        .antecedenciaMinima(bloqueio.getAntecedenciaMinima())
        .medicoCrm(bloqueio.getMedicoCrm())
        .usuarioCriadorLogin(bloqueio.getUsuarioCriadorLogin())
        .dataCriacao(bloqueio.getDataCriacao())
        .ativo(bloqueio.getAtivo())
        .build();
  }
}
