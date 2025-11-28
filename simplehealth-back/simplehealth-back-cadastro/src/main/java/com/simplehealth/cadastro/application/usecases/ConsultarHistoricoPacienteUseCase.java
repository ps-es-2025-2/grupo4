package com.simplehealth.cadastro.application.usecases;


import com.simplehealth.cadastro.application.dto.AgendamentoDTO;
import com.simplehealth.cadastro.application.dto.HistoricoPacienteDTO;
import com.simplehealth.cadastro.application.dto.ItemEstoqueDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.dto.PagamentoDTO;
import com.simplehealth.cadastro.application.dto.ProcedimentoDTO;
import com.simplehealth.cadastro.domain.events.HistoricoAgendamentoResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoEstoqueResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoPagamentoResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoProcedimentoResponseEvent;
import com.simplehealth.cadastro.infrastructure.redis.publishers.HistoricoPublisher;
import com.simplehealth.cadastro.infrastructure.repositories.PacienteRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultarHistoricoPacienteUseCase {

  private final PacienteRepository pacienteRepository;
  private final HistoricoPublisher publisher;
  private final ConcurrentHashMap<String, Object> cache;

  public HistoricoPacienteDTO execute(String cpf) {

    var paciente = pacienteRepository.findByCpf(cpf)
        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

    String cid = UUID.randomUUID().toString();

    publisher.solicitarAgendamentos(cid, cpf);
    publisher.solicitarProcedimentos(cid, cpf);
    publisher.solicitarEstoque(cid, cpf);
    publisher.solicitarPagamentos(cid, cpf);

    List<AgendamentoDTO> ag = Collections.emptyList();
    List<ProcedimentoDTO> proc = Collections.emptyList();
    List<ItemEstoqueDTO> est = Collections.emptyList();
    List<PagamentoDTO> pag = Collections.emptyList();

    int tentativas = 0;
    final int maxTentativas = 50;

    while (tentativas < maxTentativas) {

      var r1 = cache.get(cid + ":ag");
      var r2 = cache.get(cid + ":proc");
      var r3 = cache.get(cid + ":est");
      var r4 = cache.get(cid + ":pag");

      if (r1 != null || r2 != null || r3 != null || r4 != null) {

        if (r1 instanceof HistoricoAgendamentoResponseEvent) {
          ag = Optional.ofNullable(((HistoricoAgendamentoResponseEvent) r1).getAgendamentos())
              .orElse(Collections.emptyList());
          cache.remove(cid + ":ag");
        }

        if (r2 instanceof HistoricoProcedimentoResponseEvent) {
          proc = Optional.ofNullable(((HistoricoProcedimentoResponseEvent) r2).getProcedimentos())
              .orElse(Collections.emptyList());
          cache.remove(cid + ":proc");
        }

        if (r3 instanceof HistoricoEstoqueResponseEvent) {
          est = Optional.ofNullable(((HistoricoEstoqueResponseEvent) r3).getItens())
              .orElse(Collections.emptyList());
          cache.remove(cid + ":est");
        }

        if (r4 instanceof HistoricoPagamentoResponseEvent) {
          pag = Optional.ofNullable(((HistoricoPagamentoResponseEvent) r4).getPagamentos())
              .orElse(Collections.emptyList());
          cache.remove(cid + ":pag");
        }

        if (!ag.isEmpty() || !proc.isEmpty() || !est.isEmpty() || !pag.isEmpty()) {
          break;
        }
      }

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }

      tentativas++;
    }

    return HistoricoPacienteDTO.builder()
        .dadosCadastrais(new PacienteDTO(
            paciente.getId(),
            paciente.getNomeCompleto(),
            paciente.getDataNascimento(),
            paciente.getCpf(),
            paciente.getTelefone(),
            paciente.getEmail()
        ))
        .agendamentos(ag)
        .procedimentos(proc)
        .itensBaixados(est)
        .pagamentos(pag)
        .build();
  }
}
