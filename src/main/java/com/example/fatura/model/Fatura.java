package com.example.fatura.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Um processamento de PDF; agrupa as compras extraídas")
@Entity
@Data
public class Fatura {
    @Id @GeneratedValue
    @Schema(description = "Identificador da fatura", example = "1")
    private Long id;

    @Schema(description = "Data em que a fatura foi processada", example = "2026-10-13")
    private LocalDate dataUpload;

    @OneToMany(mappedBy = "fatura")
    @JsonIgnore
    private List<Compra> compras;
}