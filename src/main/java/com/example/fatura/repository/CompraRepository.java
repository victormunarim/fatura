package com.example.fatura.repository;

import com.example.fatura.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByCategoriaIgnoreCase(String categoria);
}
