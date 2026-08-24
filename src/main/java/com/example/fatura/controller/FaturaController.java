package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import com.example.fatura.service.FaturaService;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class FaturaController {

    private final FaturaService faturaService;
    private final CompraRepository lancamentoRepository;

    private final String CAMINHO = "fatura/itau_extrato.pdf";

    public FaturaController(FaturaService faturaService, CompraRepository lancamentoRepository) {
        this.faturaService = faturaService;
        this.lancamentoRepository = lancamentoRepository;
    }

    @PostMapping("/faturas")
    public List<Compra> processarFatura() throws IOException {
        List<String> linhas = faturaService.extrairLinhas(CAMINHO);
        List<Compra> lancamentos = faturaService.analisarCompras(linhas);
        return faturaService.salvar(lancamentos);
    }

    @GetMapping("/compras")
    public List<Compra> getCompras() {
        return lancamentoRepository.findAll();
    }

    @GetMapping("/compras/ordenado")
    public List<Compra> getComprasOrdenado() {
        return lancamentoRepository.findAll(Sort.by("valor"));
    }
}