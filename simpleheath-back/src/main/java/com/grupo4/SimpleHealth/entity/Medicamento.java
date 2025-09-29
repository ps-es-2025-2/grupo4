package com.grupo4.SimpleHealth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento extends Item {
    private String prescricao;
    private String composicao;
    private String bula;
    private String targa;
    private String modoConsumo;
}

