package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.service.CompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/compras/ordenado/crescente")
    public ResponseEntity<List<Compra>> getComprasOrdenadoCrescente() {
        List<Compra> compras = compraService.getAllOrdenadoCrescente();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/ordenado/decrescente")
    public ResponseEntity<List<Compra>> getComprasOrdenadoDescendente() {
        List<Compra> compras = compraService.getAllOrdenadoDescendente();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/categoria/{categoria}")
    public ResponseEntity<List<Compra>> getComprasPorCategoria(@PathVariable String categoria) {
        List<Compra> compras = compraService.findByCategoria(categoria);
        return ResponseEntity.ok(compras);
    }
}
