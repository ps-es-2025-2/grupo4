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

    // Validar se o bloqueio já passou
    if (bloqueio.getDataInicio().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Não é possível atualizar um bloqueio que já iniciou");
    }

    // Se houver mudança de período, verificar agendamentos ativos
    if (dto.getDataInicio() != null && dto.getDataFim() != null) {
      if (!dto.getDataInicio().equals(bloqueio.getDataInicio())
          || !dto.getDataFim().equals(bloqueio.getDataFim())) {
        agendamentoService.verificarAgendamentosAtivosNoPeriodo(
            bloqueio.getMedicoCrm(),
            dto.getDataInicio(),
            dto.getDataFim());
      }
      bloqueio.setDataInicio(dto.getDataInicio());
      bloqueio.setDataFim(dto.getDataFim());
    }

    // Atualizar campos permitidos
    if (dto.getMotivo() != null) {
      bloqueio.setMotivo(dto.getMotivo());
    }
    if (dto.getAntecedenciaMinima() != null) {
      bloqueio.setAntecedenciaMinima(dto.getAntecedenciaMinima());
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

