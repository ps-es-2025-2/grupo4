package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaResponseDTO;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarBloqueiosPorMedicoUseCase {

  private final BloqueioAgendaRepository bloqueioAgendaRepository;

  public List<BloqueioAgendaResponseDTO> execute(String medicoCrm) {
    List<BloqueioAgenda> bloqueios = bloqueioAgendaRepository.findByMedicoCrmAndAtivoTrueOrderByDataInicio(medicoCrm);
    return bloqueios.stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
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

