package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AtualizarBloqueioAgendaDTO;
import com.simplehealth.agendamento.application.dtos.BloqueioAgendaResponseDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarBloqueioAgendaUseCase {

  private final BloqueioAgendaRepository bloqueioAgendaRepository;
  private final AgendamentoService agendamentoService;

  public BloqueioAgendaResponseDTO execute(AtualizarBloqueioAgendaDTO dto) throws Exception {
    if (dto.getId() == null || dto.getId().isBlank()) {
      throw new IllegalArgumentException("O ID do bloqueio é obrigatório");
    }

    BloqueioAgenda bloqueio = bloqueioAgendaRepository.findById(dto.getId())
        .orElseThrow(() -> new AgendamentoException("Bloqueio de agenda não encontrado com ID: " + dto.getId()));

    // Validar se o bloqueio está ativo
    if (!bloqueio.getAtivo()) {
      throw new IllegalStateException("Não é possível atualizar um bloqueio inativo");
    }

    if (bloqueio.getDataInicio().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Não é possível atualizar um bloqueio que já iniciou");
    }

    LocalDateTime novaDataInicio = dto.getDataInicio() != null ? dto.getDataInicio() : bloqueio.getDataInicio();
    LocalDateTime novaDataFim = dto.getDataFim() != null ? dto.getDataFim() : bloqueio.getDataFim();
    String novoMedicoCrm = dto.getMedicoCrm() != null ? dto.getMedicoCrm() : bloqueio.getMedicoCrm();

    if (novaDataInicio.isAfter(novaDataFim)) {
      throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
    }

    boolean datesChanged = !novaDataInicio.equals(bloqueio.getDataInicio()) || !novaDataFim.equals(bloqueio.getDataFim());
    boolean doctorChanged = !novoMedicoCrm.equals(bloqueio.getMedicoCrm());

    if (datesChanged || doctorChanged) {
      agendamentoService.verificarAgendamentosAtivosNoPeriodo(
          novoMedicoCrm,
          novaDataInicio,
          novaDataFim);

      bloqueio.setDataInicio(novaDataInicio);
      bloqueio.setDataFim(novaDataFim);
      bloqueio.setMedicoCrm(novoMedicoCrm);
    }

    if (dto.getMotivo() != null) {
      bloqueio.setMotivo(dto.getMotivo());
    }
    if (dto.getAntecedenciaMinima() != null) {
      bloqueio.setAntecedenciaMinima(dto.getAntecedenciaMinima());
    }
    if (dto.getUsuarioCriadorLogin() != null) {
      bloqueio.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    }
    if (dto.getDataCriacao() != null) {
      bloqueio.setDataCriacao(dto.getDataCriacao());
    }
    if (dto.getAtivo() != null) {
      bloqueio.setAtivo(dto.getAtivo());
    }

    BloqueioAgenda atualizado = bloqueioAgendaRepository.save(bloqueio);

    return toResponseDTO(atualizado);
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

