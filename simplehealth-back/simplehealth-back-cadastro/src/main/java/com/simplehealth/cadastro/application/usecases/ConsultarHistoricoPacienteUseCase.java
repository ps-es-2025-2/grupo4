package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.ConsultaDTO;
import com.simplehealth.cadastro.application.dto.ExameDTO;
import com.simplehealth.cadastro.application.dto.HistoricoPacienteDTO;
import com.simplehealth.cadastro.application.dto.ItemEstoqueDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.dto.PagamentoDTO;
import com.simplehealth.cadastro.application.dto.ProcedimentoDTO;
import com.simplehealth.cadastro.domain.events.HistoricoConsultaResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoExameResponseEvent;
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

    publisher.solicitarConsultas(cid, cpf);
    publisher.solicitarExames(cid, cpf);
    publisher.solicitarProcedimentos(cid, cpf);
    publisher.solicitarEstoque(cid, cpf);
    publisher.solicitarPagamentos(cid, cpf);

    List<ConsultaDTO> cons = Collections.emptyList();
    List<ExameDTO> exam = Collections.emptyList();
    List<ProcedimentoDTO> proc = Collections.emptyList();
    List<ItemEstoqueDTO> est = Collections.emptyList();
    List<PagamentoDTO> pag = Collections.emptyList();

    int tentativas = 0;
    final int maxTentativas = 50;
    
    boolean consultasRecebidas = false;
    boolean examesRecebidos = false;
    boolean procedimentosRecebidos = false;

    while (tentativas < maxTentativas && (!consultasRecebidas || !examesRecebidos || !procedimentosRecebidos)) {

      var r1 = cache.get(cid + ":cons");
      var r2 = cache.get(cid + ":exam");
      var r3 = cache.get(cid + ":proc");
      var r4 = cache.get(cid + ":est");
      var r5 = cache.get(cid + ":pag");

      if (r1 instanceof HistoricoConsultaResponseEvent) {
        cons = Optional.ofNullable(((HistoricoConsultaResponseEvent) r1).getConsultas())
            .orElse(Collections.emptyList());
        cache.remove(cid + ":cons");
        consultasRecebidas = true;
      }

      if (r2 instanceof HistoricoExameResponseEvent) {
        exam = Optional.ofNullable(((HistoricoExameResponseEvent) r2).getExames())
            .orElse(Collections.emptyList());
        cache.remove(cid + ":exam");
        examesRecebidos = true;
      }

      if (r3 instanceof HistoricoProcedimentoResponseEvent) {
        proc = Optional.ofNullable(((HistoricoProcedimentoResponseEvent) r3).getProcedimentos())
            .orElse(Collections.emptyList());
        cache.remove(cid + ":proc");
        procedimentosRecebidos = true;
      }

      if (r4 instanceof HistoricoEstoqueResponseEvent) {
        est = Optional.ofNullable(((HistoricoEstoqueResponseEvent) r4).getItens())
            .orElse(Collections.emptyList());
        cache.remove(cid + ":est");
      }

      if (r5 instanceof HistoricoPagamentoResponseEvent) {
        pag = Optional.ofNullable(((HistoricoPagamentoResponseEvent) r5).getPagamentos())
            .orElse(Collections.emptyList());
        cache.remove(cid + ":pag");
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
            paciente.getEmail(),
            paciente.getConvenio() != null ? paciente.getConvenio().getId() : null,
            paciente.getConvenio() != null ? paciente.getConvenio().getNome() : null))
        .consultas(cons)
        .exames(exam)
        .procedimentos(proc)
        .itensBaixados(est)
        .pagamentos(pag)
        .build();
  }
}
