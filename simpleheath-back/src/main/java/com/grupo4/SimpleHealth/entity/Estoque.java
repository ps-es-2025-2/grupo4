package com.grupo4.SimpleHealth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstoque;

    private String local;

    @OneToOne
    @JoinColumn(name = "id_item")
    private Item item;
}
