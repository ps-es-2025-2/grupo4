package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.AgendamentoRepository;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

  private final AgendamentoRepository agendamentoRepository;
  private final BloqueioAgendaRepository bloqueioRepository;

  public Agendamento salvar(Agendamento agendamento) {
    return agendamentoRepository.save(agendamento);
  }

  public Optional<Agendamento> buscarPorId(String id) {
    return agendamentoRepository.findById(id);
  }

  public void verificarDisponibilidade(String medicoCrm, LocalDateTime inicio, LocalDateTime fim) throws Exception {

    if (!agendamentoRepository.findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
        medicoCrm, inicio, fim, StatusAgendamentoEnum.ATIVO).isEmpty()) {
      throw new Exception("Horário indisponível.");
    }

    if (!bloqueioRepository.findByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        medicoCrm, inicio, fim).isEmpty()) {
      throw new Exception("Horário bloqueado.");
    }
  }

  public List<Agendamento> buscarHistorico(String cpf) {
    return agendamentoRepository.findByPacienteCpfOrderByDataHoraInicioDesc(cpf);
  }

  public void validarCancelamento(Agendamento agendamento, String motivo) {

    if (agendamento.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("O agendamento já está cancelado.");
    }

    if (motivo == null || motivo.isBlank()) {
      throw new IllegalArgumentException("O motivo do cancelamento é obrigatório.");
    }

    if (agendamento.getDataHoraInicio().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Não é possível cancelar um agendamento que já ocorreu.");
    }
  }
}
