package com.simplehealth.agendamento.domain.entity;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Procedimento")
public class Procedimento extends Agendamento {

  @Property("descricaoProcedimento")
  private String descricaoProcedimento;

  @Property("salaEquipamentoNecessario")
  private String salaEquipamentoNecessario;

  @Property("nivelRisco")
  private String nivelRisco;

  public String getDescricaoProcedimento() {
    return descricaoProcedimento;
  }

  public void setDescricaoProcedimento(String descricaoProcedimento) {
    this.descricaoProcedimento = descricaoProcedimento;
  }

  public String getSalaEquipamentoNecessario() {
    return salaEquipamentoNecessario;
  }

  public void setSalaEquipamentoNecessario(String salaEquipamentoNecessario) {
    this.salaEquipamentoNecessario = salaEquipamentoNecessario;
  }

  public String getNivelRisco() {
    return nivelRisco;
  }

  public void setNivelRisco(String nivelRisco) {
    this.nivelRisco = nivelRisco;
  }
}
