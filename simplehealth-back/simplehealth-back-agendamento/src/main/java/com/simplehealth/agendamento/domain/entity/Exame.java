package com.simplehealth.agendamento.domain.entity;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Exame")
public class Exame extends Agendamento {

  @Property("nomeExame")
  private String nomeExame;

  @Property("requerPreparo")
  private Boolean requerPreparo;

  @Property("instrucoesPreparo")
  private String instrucoesPreparo;

  public String getNomeExame() {
    return nomeExame;
  }

  public void setNomeExame(String nomeExame) {
    this.nomeExame = nomeExame;
  }

  public Boolean getRequerPreparo() {
    return requerPreparo;
  }

  public void setRequerPreparo(Boolean requerPreparo) {
    this.requerPreparo = requerPreparo;
  }

  public String getInstrucoesPreparo() {
    return instrucoesPreparo;
  }

  public void setInstrucoesPreparo(String instrucoesPreparo) {
    this.instrucoesPreparo = instrucoesPreparo;
  }
}
