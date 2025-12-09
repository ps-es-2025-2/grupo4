package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarBloqueioAgendaPorIdUseCase {

  private final BloqueioAgendaRepository bloqueioAgendaRepository;

  public BloqueioAgendaResponseDTO execute(String id) {
    BloqueioAgenda bloqueio = bloqueioAgendaRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Bloqueio de agenda não encontrado com ID: " + id));

    return toResponseDTO(bloqueio);
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

