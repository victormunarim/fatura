package com.example.fatura.controller;

import com.example.fatura.service.FaturaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class FaturaController {

    private final FaturaService faturaService;

    private final String CAMINHO = "fatura/itau_extrato.pdf";

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @GetMapping("/compras")
    public List<Map<String, String>> getFatura() throws IOException {
        List<String> linhas = faturaService.extrairLinhas(CAMINHO);
        return faturaService.analisarCompras(linhas);
    }

    @GetMapping("/compras/ordenado")
    public List<Map<String, String>> getComprasOrdenado() throws IOException {
        List<String> linhas = faturaService.extrairLinhas(CAMINHO);
        return faturaService.analisarComprasOrdenado(linhas);
    }
}
