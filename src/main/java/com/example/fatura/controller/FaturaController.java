package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.service.FaturaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class FaturaController {

    private final FaturaService faturaService;

    private final String CAMINHO = "fatura/itau_extrato.pdf";

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @PostMapping("/faturas")
    public List<Compra> processarFatura() throws IOException {
        List<String> linhas = faturaService.extrairLinhas(CAMINHO);
        List<Compra> compras = faturaService.analisarCompras(linhas);
        return faturaService.salvar(compras);
    }
}