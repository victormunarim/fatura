package com.example.fatura.dto;

import lombok.Data;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

// nao é uma entidade: nao vira tabela no banco, os valores são calculados na hora
@Schema(description = "Gasto total de uma categoria e sua participação no total de gastos")
@Data
public class CategoriaGasto {
    @Schema(description = "Nome da categoria", example = "Pix")
    private String categoria;

    @Schema(description = "Soma dos gastos da categoria", example = "1229.01")
    private BigDecimal total;

    @Schema(description = "Percentual sobre o total de gastos (0 a 100)", example = "34.17")
    private double percentual;
}