package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.service.FaturaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class FaturaController {

    private final FaturaService faturaService;

    private final String CAMINHO = "fatura/itau_extrato.pdf";

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @PostMapping("/faturas")
    public ResponseEntity<List<Compra>> processarFatura() throws IOException {
        List<String> linhas = faturaService.extrairLinhas(CAMINHO);
        List<Compra> compras = faturaService.analisarCompras(linhas);
        List<Compra> saved = faturaService.salvar(compras);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/faturas/deleta")
    public ResponseEntity<Void> deleteAll() {
        faturaService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}