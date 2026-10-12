package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // sobe a aplicação inteira, com o banco H2
@AutoConfigureMockMvc // permite simular requisições HTTP sem abrir o servidor
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompraRepository compraRepository;

    // antes de cada teste, limpa o banco e insere compras com valores conhecidos
    @BeforeEach
    void prepararDados() {
        compraRepository.deleteAll();
        salvarCompra("Pix", "-50.00");
        salvarCompra("Pix", "-25.00");
        salvarCompra("Tarifa", "-25.00");
        salvarCompra("Rendimento", "100.00"); // entrada: não conta como gasto
    }

    private void salvarCompra(String categoria, String valor) {
        Compra compra = new Compra();
        compra.setCategoria(categoria);
        compra.setValor(new BigDecimal(valor));
        compraRepository.save(compra);
    }

    @Test
    void deveDevolverACategoriasComTotalEPercentual() throws Exception {
        mockMvc.perform(get("/compras/distribuicao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoria").value("Pix"))
                .andExpect(jsonPath("$[0].total").value(75.0))
                .andExpect(jsonPath("$[0].percentual").value(75.0))
                .andExpect(jsonPath("$[1].categoria").value("Tarifa"))
                .andExpect(jsonPath("$[1].percentual").value(25.0));
    }

    @Test
    void deveDevolver404QuandoNaoHaGastos() throws Exception {
        compraRepository.deleteAll(); // banco vazio

        mockMvc.perform(get("/compras/distribuicao"))
                .andExpect(status().isNotFound());
    }
}