package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import org.springframework.data.domain.Sort;
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
    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }

    @GetMapping("/compras/ordenado")
    public List<Compra> getComprasOrdenado() {
        return compraRepository.findAll(Sort.by("valor"));
    }
}