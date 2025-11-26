package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;


@Node("Consulta")
public class Consulta extends Agendamento {

  @Property("especialidade")
  private String especialidade;

  @Property("tipoConsulta")
  private TipoConsultaEnum tipoConsulta;

  public String getEspecialidade() {
    return especialidade;
  }

  public void setEspecialidade(String especialidade) {
    this.especialidade = especialidade;
  }

  public TipoConsultaEnum getTipoConsulta() {
    return tipoConsulta;
  }

  public void setTipoConsulta(TipoConsultaEnum tipoConsulta) {
    this.tipoConsulta = tipoConsulta;
  }
}
