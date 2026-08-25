package com.example.fatura.service;

import com.example.fatura.exception.ResourceNotFoundException;
import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public List<Compra> getAll() {
        List<Compra> compras = compraRepository.findAll();

        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma compra encontrada");
        }

        return compras;
    }

    public List<Compra> getAllOrdenadoCrescente() {
        List<Compra> compras = compraRepository.findAll(Sort.by("valor"));

        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma compra encontrada");
        }

        return compras;
    }

    public List<Compra> getAllOrdenadoDescendente() {
        List<Compra> compras = compraRepository.findAll(Sort.by(Sort.Direction.DESC, "valor"));

        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma compra encontrada");
        }

        return compras;
    }
    public List<Compra> findByCategoria(String categoria) {
        List<Compra> compras = compraRepository.findByCategoriaIgnoreCase(categoria);

        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma compra encontrada para a categoria: " + categoria);
        }

        return compras;
    }
}

