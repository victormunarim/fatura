package com.example.fatura.service;

import com.example.fatura.model.Compra;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FaturaServiceTest {

    private final FaturaService faturaService = new FaturaService(null, null);

    @Test
    void deveExtrairDataDescricaoValorECategoriaDeUmaLinhaValida() {
        List<String> linhas = List.of("10/08/2026 PIX TRANSF RENATA 10/08 -120,00");

        List<Compra> compras = faturaService.analisarCompras(linhas);

        assertEquals(1, compras.size());
        Compra compra = compras.get(0);
        assertEquals("PIX TRANSF RENATA 10/08", compra.getDescricao());
        assertEquals(new BigDecimal("-120.00"), compra.getValor());
        assertEquals("Pix", compra.getCategoria());
    }

    @Test
    void deveIgnorarLinhaDeSaldoDoDia() {
        List<String> linhas = List.of("18/08/2026 SALDO DO DIA 247,41");

        List<Compra> compras = faturaService.analisarCompras(linhas);

        assertTrue(compras.isEmpty());
    }

    @Test
    void deveClassificarPorPrefixoDaDescricao() {
        List<String> linhas = List.of(
                "04/08/2026 TAR PACOTE ITAU JUL/26 -16,10",
                "10/08/2026 REND PAGO APLIC AUT MAIS 0,03",
                "17/08/2026 FATURA PAGA ITAU MULTIPL -2.351,54",
                "06/08/2026 TED 001.5201.VICTOR E P 2.883,42"
        );

        List<Compra> compras = faturaService.analisarCompras(linhas);

        assertEquals("Tarifa", compras.get(0).getCategoria());
        assertEquals("Rendimento", compras.get(1).getCategoria());
        assertEquals("Pagamento de fatura", compras.get(2).getCategoria());
        assertEquals("Transferência", compras.get(3).getCategoria());
    }
}
