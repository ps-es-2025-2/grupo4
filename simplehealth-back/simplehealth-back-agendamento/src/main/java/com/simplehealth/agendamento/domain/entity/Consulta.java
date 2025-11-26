package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "consulta")
public class Consulta extends Agendamento {

  private String especialidade;

  private TipoConsultaEnum tipoConsulta;
}
