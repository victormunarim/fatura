package com.example.fatura.controller;

import com.example.fatura.exception.ResourceNotFoundException;
import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompraController {

    private final CompraRepository compraRepository;

    public CompraController(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @GetMapping("/compras")
    public ResponseEntity<List<Compra>> getCompras() {
        List<Compra> compras = compraRepository.findAll();

        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma compra encontrada");
        }

        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/ordenado")
    public ResponseEntity<List<Compra>> getComprasOrdenado() {
        List<Compra> compras = compraRepository.findAll(Sort.by("valor"));

        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma compra encontrada");
        }

        return ResponseEntity.ok(compras);
    }
}