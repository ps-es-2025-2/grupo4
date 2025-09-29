package com.grupo4.SimpleHealth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospitalar extends Item {
    private String descricao;
    private boolean descartabilidade;
    private String uso;
}
