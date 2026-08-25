package com.example.fatura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Fatura {
    @Id @GeneratedValue
    private Long id;
    private LocalDate dataUpload;

    @OneToMany(mappedBy = "fatura")
    private List<Compra> compras;
}