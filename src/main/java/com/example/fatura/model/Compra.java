package com.example.fatura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lançamento extraído de uma fatura ou extrato")
@Entity
@Data
public class Compra {
    @Id
    @GeneratedValue
    @Schema(description = "Identificador da compra", example = "1")
    private Long id;

    @Schema(description = "Data do lançamento", example = "2026-08-10")
    private LocalDate data;

    @Schema(description = "Valor do lançamento (negativo = gasto, positivo = entrada)", example = "-120.00")
    private BigDecimal valor;

    @Schema(description = "Categoria atribuída pelo início da descrição", example = "Pix")
    private String categoria;

    @Schema(description = "Descrição extraída do PDF", example = "PIX TRANSF RENATA 10/08")
    private String descricao;

    @ManyToOne
    @Schema(description = "Fatura (processamento do PDF) à qual a compra pertence")
    private Fatura fatura;
}
