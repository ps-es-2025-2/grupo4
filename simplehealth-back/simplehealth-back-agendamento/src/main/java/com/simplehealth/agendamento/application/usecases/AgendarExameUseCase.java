package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendarExameDTO;
import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ExameService;
import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgendarExameUseCase {

  private final AgendamentoService agendamentoService;
  private final ExameService exameService;

  public ExameResponseDTO execute(AgendarExameDTO dto) throws Exception {
    // Verifica disponibilidade baseado nas datas previstas
    if (dto.getDataHoraInicioPrevista() != null && dto.getDataHoraFimPrevista() != null) {
      agendamentoService.verificarDisponibilidade(
          dto.getMedicoCrm(),
          dto.getDataHoraInicioPrevista(),
          dto.getDataHoraFimPrevista());
    }

    Exame exame = new Exame();
    exame.setPacienteCpf(dto.getPacienteCpf());
    exame.setMedicoCrm(dto.getMedicoCrm());
    exame.setDataHoraAgendamento(LocalDateTime.now());
    exame.setDataHoraInicioPrevista(dto.getDataHoraInicioPrevista());
    exame.setDataHoraFimPrevista(dto.getDataHoraFimPrevista());
    // dataHoraInicioExecucao e dataHoraFimExecucao serão definidos ao iniciar/finalizar serviço
    exame.setNomeExame(dto.getNomeExame());
    exame.setRequerPreparo(dto.getRequerPreparo());
    exame.setInstrucoesPreparo(dto.getInstrucoesPreparo());
    exame.setConvenioNome(dto.getConvenioNome());
    exame.setModalidade(dto.getModalidade());
    exame.setObservacoes(dto.getObservacoes());
    exame.setUsuarioCriadorLogin(dto.getUsuarioCriadorLogin());
    exame.setStatus(StatusAgendamentoEnum.ATIVO);

    Exame salvo = exameService.salvar(exame);

    return toResponseDTO(salvo);
  }

  private ExameResponseDTO toResponseDTO(Exame exame) {
    return ExameResponseDTO.builder()
        .id(exame.getId())
        .dataHoraAgendamento(exame.getDataHoraAgendamento())
        .dataHoraInicioPrevista(exame.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(exame.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(exame.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(exame.getDataHoraFimExecucao())
        .isEncaixe(exame.getIsEncaixe())
        .modalidade(exame.getModalidade())
        .motivoEncaixe(exame.getMotivoEncaixe())
        .observacoes(exame.getObservacoes())
        .status(exame.getStatus())
        .motivoCancelamento(exame.getMotivoCancelamento())
        .dataCancelamento(exame.getDataCancelamento())
        .pacienteCpf(exame.getPacienteCpf())
        .medicoCrm(exame.getMedicoCrm())
        .convenioNome(exame.getConvenioNome())
        .usuarioCriadorLogin(exame.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(exame.getUsuarioCanceladorLogin())
        .nomeExame(exame.getNomeExame())
        .requerPreparo(exame.getRequerPreparo())
        .instrucoesPreparo(exame.getInstrucoesPreparo())
        .build();
  }
}