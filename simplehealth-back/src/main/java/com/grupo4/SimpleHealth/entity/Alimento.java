package com.grupo4.SimpleHealth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alimento extends Item {
    private String alergenicos;
    private String tipoArmazenamento;
}
