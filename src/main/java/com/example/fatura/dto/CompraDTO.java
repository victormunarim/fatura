package com.example.fatura.dto;

import com.example.fatura.model.Compra;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

//  a api devolve só os campos da compra, sem a Fatura
@Data
public class CompraDTO {
    private Long id;
    private LocalDate data;
    private String descricao;
    private BigDecimal valor;
    private String categoria;

    // converte a entidade Compra no DTO
    public static CompraDTO de(Compra c) {
        CompraDTO r = new CompraDTO();
        r.setId(c.getId());
        r.setData(c.getData());
        r.setDescricao(c.getDescricao());
        r.setValor(c.getValor());
        r.setCategoria(c.getCategoria());
        return r;
    }
}