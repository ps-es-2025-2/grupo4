package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendarProcedimentoDTO;
import com.simplehealth.agendamento.application.dtos.ProcedimentoResponseDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ProcedimentoService;
import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgendarProcedimentoUseCase {

  private final AgendamentoService agendamentoService;
  private final ProcedimentoService procedimentoService;

  public ProcedimentoResponseDTO execute(AgendarProcedimentoDTO dto) throws Exception {
    // Verifica disponibilidade baseado nas datas previstas
    if (dto.getDataHoraInicioPrevista() != null && dto.getDataHoraFimPrevista() != null) {
      agendamentoService.verificarDisponibilidade(
          dto.getMedicoCrm(),
          dto.getDataHoraInicioPrevista(),
          dto.getDataHoraFimPrevista());
    }

    Procedimento procedimento = new Procedimento();
    procedimento.setPacienteCpf(dto.getPacienteCpf());
    procedimento.setMedicoCrm(dto.getMedicoCrm());
    procedimento.setDataHoraAgendamento(LocalDateTime.now());
    procedimento.setDataHoraInicioPrevista(dto.getDataHoraInicioPrevista());
    procedimento.setDataHoraFimPrevista(dto.getDataHoraFimPrevista());
    // dataHoraInicioExecucao e dataHoraFimExecucao serão definidos ao iniciar/finalizar serviço
    procedimento.setDescricaoProcedimento(dto.getDescricaoProcedimento());
    procedimento.setSalaEquipamentoNecessario(dto.getSalaEquipamentoNecessario());
    procedimento.setNivelRisco(dto.getNivelRisco());
    procedimento.setConvenioNome(dto.getConvenioNome());
    procedimento.setModalidade(dto.getModalidade());
    procedimento.setObservacoes(dto.getObservacoes());
    procedimento.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    procedimento.setStatus(StatusAgendamentoEnum.ATIVO);

    Procedimento salvo = procedimentoService.salvar(procedimento);

    return toResponseDTO(salvo);
  }

  private ProcedimentoResponseDTO toResponseDTO(Procedimento procedimento) {
    return ProcedimentoResponseDTO.builder()
        .id(procedimento.getId())
        .dataHoraAgendamento(procedimento.getDataHoraAgendamento())
        .dataHoraInicioPrevista(procedimento.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(procedimento.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(procedimento.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(procedimento.getDataHoraFimExecucao())
        .isEncaixe(procedimento.getIsEncaixe())
        .modalidade(procedimento.getModalidade())
        .motivoEncaixe(procedimento.getMotivoEncaixe())
        .observacoes(procedimento.getObservacoes())
        .status(procedimento.getStatus())
        .motivoCancelamento(procedimento.getMotivoCancelamento())
        .dataCancelamento(procedimento.getDataCancelamento())
        .pacienteCpf(procedimento.getPacienteCpf())
        .medicoCrm(procedimento.getMedicoCrm())
        .convenioNome(procedimento.getConvenioNome())
        .usuarioCriadorLogin(procedimento.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(procedimento.getUsuarioCanceladorLogin())
        .descricaoProcedimento(procedimento.getDescricaoProcedimento())
        .salaEquipamentoNecessario(procedimento.getSalaEquipamentoNecessario())
        .nivelRisco(procedimento.getNivelRisco())
        .build();
  }
}