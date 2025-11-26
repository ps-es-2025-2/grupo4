package com.simplehealth.cadastro.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPacienteDTO {

  private PacienteDTO dadosCadastrais;

  private PessoaDTO pessoa;

  private List<AgendamentoDTO> agendamentos;
  private List<ProcedimentoDTO> procedimentos;
  private List<ItemEstoqueDTO> itensBaixados;
  private List<PagamentoDTO> pagamentos;
}
