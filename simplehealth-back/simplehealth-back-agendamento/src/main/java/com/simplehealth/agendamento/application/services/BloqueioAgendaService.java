package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BloqueioAgendaService {

  private final BloqueioAgendaRepository bloqueioRepository;

  public boolean existemAgendamentosAtivos(String crm, LocalDateTime inicio, LocalDateTime fim) {
    return bloqueioRepository.existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        crm, inicio, fim
    );
  }

  public BloqueioAgenda salvar(BloqueioAgenda entidade) {
    return bloqueioRepository.save(entidade);
  }
}
