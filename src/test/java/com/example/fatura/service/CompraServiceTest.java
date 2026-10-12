package com.example.fatura.service;

import com.example.fatura.dto.CategoriaGasto;
import com.example.fatura.model.Compra;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompraServiceTest {

    // calcularDistribuicao não usa o banco, então o repository pode ser null no teste
    private final CompraService service = new CompraService(null);

    // metodo auxiliar para criar uma compra de teste rapidamente
    private Compra criarCompra(String categoria, String valor) {
        Compra compra = new Compra();
        compra.setCategoria(categoria);
        compra.setValor(new BigDecimal(valor));
        return compra;
    }

    @Test
    void deveSomarOsGastosDeCadaCategoria() {
        List<Compra> compras = List.of(
                criarCompra("Pix", "-10.00"),
                criarCompra("Pix", "-20.00"),
                criarCompra("Tarifa", "-5.00"));

        List<CategoriaGasto> resultado = service.calcularDistribuicao(compras);

        assertEquals(2, resultado.size()); // duas categorias: Pix e Tarifa
        // o 0.001 é a margem de erro aceita ao comparar números com vírgula (double)
        assertEquals(30.0, resultado.get(0).getTotal().doubleValue(), 0.001); // Pix: 10 + 20
        assertEquals(5.0, resultado.get(1).getTotal().doubleValue(), 0.001); // Tarifa
    }

    @Test
    void deveCalcularOPercentualDeCadaCategoria() {
        List<Compra> compras = List.of(
                criarCompra("Pix", "-75.00"),
                criarCompra("Tarifa", "-25.00")); // total geral = 100

        List<CategoriaGasto> resultado = service.calcularDistribuicao(compras);

        assertEquals(75.0, resultado.get(0).getPercentual(), 0.001);
        assertEquals(25.0, resultado.get(1).getPercentual(), 0.001);
    }

    @Test
    void deveOrdenarDoMaiorGastoParaOMenor() {
        List<Compra> compras = List.of(
                criarCompra("Tarifa", "-5.00"),
                criarCompra("Pix", "-50.00"),
                criarCompra("Saque", "-20.00"));

        List<CategoriaGasto> resultado = service.calcularDistribuicao(compras);

        assertEquals("Pix", resultado.get(0).getCategoria());
        assertEquals("Saque", resultado.get(1).getCategoria());
        assertEquals("Tarifa", resultado.get(2).getCategoria());
    }

    @Test
    void deveIgnorarEntradasEDevolverListaVaziaSemGastos() {
        // valor positivo é entrada (ex.: rendimento), não é gasto
        List<Compra> soEntradas = List.of(criarCompra("Rendimento", "100.00"));

        assertTrue(service.calcularDistribuicao(soEntradas).isEmpty());
        assertTrue(service.calcularDistribuicao(List.of()).isEmpty());
    }
}