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
import java.util.List;
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

    List<AgendamentoDTO> ag = null;
    List<ProcedimentoDTO> proc = null;
    List<ItemEstoqueDTO> est = null;
    List<PagamentoDTO> pag = null;

    int tentativas = 0;

    while (tentativas < 50) {

      var r1 = cache.get(cid + ":ag");
      var r2 = cache.get(cid + ":proc");
      var r3 = cache.get(cid + ":est");
      var r4 = cache.get(cid + ":pag");

      if (r1 != null && r2 != null && r3 != null && r4 != null) {

        ag = ((HistoricoAgendamentoResponseEvent) r1).getAgendamentos();
        proc = ((HistoricoProcedimentoResponseEvent) r2).getProcedimentos();
        est = ((HistoricoEstoqueResponseEvent) r3).getItens();
        pag = ((HistoricoPagamentoResponseEvent) r4).getPagamentos();

        cache.remove(cid + ":ag");
        cache.remove(cid + ":proc");
        cache.remove(cid + ":est");
        cache.remove(cid + ":pag");

        break;
      }

      try {
        Thread.sleep(100);
      } catch (Exception ignored) {
      }
      tentativas++;
    }

    if (ag == null || proc == null || est == null || pag == null) {
      throw new RuntimeException("Timeout ao consultar histórico.");
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
