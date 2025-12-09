package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Agendamento;
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

    if (!agendamentoRepository.findByMedicoCrmAndDataHoraInicioPrevistaLessThanEqualAndDataHoraFimPrevistaGreaterThanEqualAndStatus(
        medicoCrm, inicio, fim, StatusAgendamentoEnum.ATIVO).isEmpty()) {
      throw new Exception("Horário indisponível.");
    }

    // Verificação de bloqueio desabilitada temporariamente
    // TODO: Corrigir query de bloqueio no futuro
    /*
    try {
      if (bloqueioRepository.existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanAndDataFimGreaterThan(
          medicoCrm, fim, inicio)) {
        throw new Exception("Horário bloqueado.");
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().contains("bloqueado")) {
        throw e;
      }
    }
    */
  }

  public List<Agendamento> buscarHistorico(String cpf) {
    return agendamentoRepository.findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(cpf);
  }

  public void validarCancelamento(Agendamento agendamento, String motivo) {

    if (agendamento.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("O agendamento já está cancelado.");
    }

    if (motivo == null || motivo.isBlank()) {
      throw new IllegalArgumentException("O motivo do cancelamento é obrigatório.");
    }

    // Verifica se já foi executado (iniciado)
    if (agendamento.getDataHoraInicioExecucao() != null &&
        agendamento.getDataHoraInicioExecucao().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Não é possível cancelar um agendamento que já foi iniciado.");
    }
  }

  public void verificarAgendamentosAtivosNoPeriodo(String medicoCrm, LocalDateTime inicio, LocalDateTime fim)
      throws Exception {
    List<Agendamento> agendamentosAtivos = agendamentoRepository
        .findByMedicoCrmAndDataHoraInicioPrevistaLessThanEqualAndDataHoraFimPrevistaGreaterThanEqualAndStatus(
            medicoCrm, fim, inicio, StatusAgendamentoEnum.ATIVO);

    if (!agendamentosAtivos.isEmpty()) {
      throw new Exception("Bloqueio não permitido. " + agendamentosAtivos.size() +
          " agendamento(s) ativo(s) devem ser cancelados ou reagendados primeiro.");
    }
  }
}
