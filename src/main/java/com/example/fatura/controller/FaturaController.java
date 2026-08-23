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

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @GetMapping("/compras")
    public List<Map<String, String>> getFatura() throws IOException {
        List<String> lines = faturaService.extrairLinhas("fatura/fatura.pdf");
        return faturaService.analisarCompras(lines);
    }
}
