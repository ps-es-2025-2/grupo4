package com.grupo4.SimpleHealth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idPedido;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dataPedido;

  private String status;

  @ManyToOne
  @JoinColumn(name = "id_fornecedor")
  private Fornecedor fornecedor;

  @ManyToMany
  @JoinTable(
      name = "pedido_item",
      joinColumns = @JoinColumn(name = "id_pedido"),
      inverseJoinColumns = @JoinColumn(name = "id_item")
  )
  private List<Item> itens;
}
