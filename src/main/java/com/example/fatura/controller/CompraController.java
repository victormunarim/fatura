package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.service.CompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping("/compras")
    public ResponseEntity<List<Compra>> getCompras() {
        List<Compra> compras = compraService.getAll();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/ordenado")
    public ResponseEntity<List<Compra>> getComprasOrdenado() {
        List<Compra> compras = compraService.getAllOrdenado();
        return ResponseEntity.ok(compras);
    }
}