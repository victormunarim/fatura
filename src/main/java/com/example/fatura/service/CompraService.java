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
        return compraRepository.findAll();
    }

    public List<Compra> getAllOrdenadoCrescente() {
        return compraRepository.findAll(Sort.by("valor"));
    }

    public List<Compra> getAllOrdenadoDescendente() {
        return compraRepository.findAll(Sort.by(Sort.Direction.DESC, "valor"));
    }
    public List<Compra> findByCategoria(String categoria) {
        return compraRepository.findByCategoriaIgnoreCase(categoria);
    }

    public Compra findById(Long id){
        return compraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada: " + id));
    }

    public void deleteById(Long id){
        findById(id); // se não existir ja lança 404
        compraRepository.deleteById(id);
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

    public Compra atualizarCategoria(Long id, String categoria){
        Compra compra = findById(id);
        compra.setCategoria(categoria);
        return compraRepository.save(compra);
    }
}

