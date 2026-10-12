package com.example.fatura.dto;

import lombok.Data;

import java.math.BigDecimal;

// nao é uma entidade: nao vira tabela no banco, os valores são calculados na hora
@Data
public class CategoriaGasto {
    private String categoria;
    private BigDecimal total;
    private double percentual;
}