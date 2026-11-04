package com.example.fatura.dto;

import lombok.Data;

// corpo do patch: {"categoria": "Mercado"}
@Data
public class AtualizaCategoriaRequest {
    private String categoria;
}