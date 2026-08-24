package com.example.fatura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Compra {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate data;
    private BigDecimal valor;
    private String categoria;
    private String descricao;

    @ManyToOne
    private Fatura fatura;
}
