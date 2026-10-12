package com.example.fatura.service;

import com.example.fatura.exception.ResourceNotFoundException;
import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.fatura.dto.CategoriaGasto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // busca as compras no banco e devolve a distribuição dos gastos por categoria
    public List<CategoriaGasto> getDistribuicaoGastos() {
        List<Compra> compras = compraRepository.findAll();
        List<CategoriaGasto> distribuicao = calcularDistribuicao(compras);

        if (distribuicao.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum gasto encontrado");
        }

        return distribuicao;
    }

    // só faz a conta, sem acessar o banco. Separado para poder ser testado sozinho.
    public List<CategoriaGasto> calcularDistribuicao(List<Compra> compras) {

        Map<String, BigDecimal> totalPorCategoria = new HashMap<>();
        BigDecimal totalGeral = BigDecimal.ZERO;

        for (Compra compra : compras) {
            if (compra.getValor().signum() < 0) { // signum() < 0 significa "é negativo"
                BigDecimal gasto = compra.getValor().abs(); // tira o sinal negativo

                BigDecimal somaAtual = totalPorCategoria.get(compra.getCategoria());
                if (somaAtual == null) {
                    somaAtual = BigDecimal.ZERO; // primeira compra dessa categoria
                }

                totalPorCategoria.put(compra.getCategoria(), somaAtual.add(gasto));
                totalGeral = totalGeral.add(gasto);
            }
        }

        // transforma cada categoria em um CategoriaGasto, já com o percentual
        List<CategoriaGasto> resultado = new ArrayList<>();

        for (String categoria : totalPorCategoria.keySet()) {
            BigDecimal total = totalPorCategoria.get(categoria);

            double percentual = total.doubleValue() / totalGeral.doubleValue() * 100;
            percentual = Math.round(percentual * 100.0) / 100.0; // arredonda para 2 casas

            CategoriaGasto item = new CategoriaGasto();
            item.setCategoria(categoria);
            item.setTotal(total);
            item.setPercentual(percentual);
            resultado.add(item);
        }

        // ordena do maior gasto para o menor (os "principais gastos" ficam primeiro)
        resultado.sort((a, b) -> b.getTotal().compareTo(a.getTotal()));

        return resultado;
    }
}

