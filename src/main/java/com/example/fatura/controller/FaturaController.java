package com.example.fatura.controller;

import com.example.fatura.model.Compra;
import com.example.fatura.service.FaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

@Tag(name = "Faturas", description = "Processamento e exclusão das faturas")
@RestController
public class FaturaController {

    private final FaturaService faturaService;

    private final String CAMINHO = "fatura/itau_extrato.pdf";

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @Operation(summary = "Processa o PDF e salva as compras",
            description = "Lê o arquivo fixo fatura/itau_extrato.pdf, extrai data, descrição e valor de cada lançamento, classifica a categoria, cria uma Fatura e salva as compras. Cada chamada cria uma nova fatura: chamar duas vezes duplica as compras.")
    @ApiResponse(responseCode = "200", description = "Compras criadas")
    @ApiResponse(responseCode = "500", description = "Erro ao ler o PDF (arquivo ausente ou inválido)",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Erro ao ler ou processar arquivo: fatura/itau_extrato.pdf")))
    @PostMapping("/faturas")
    public ResponseEntity<List<Compra>> processarFatura() throws IOException {
        List<String> linhas = faturaService.extrairLinhas(CAMINHO);
        List<Compra> compras = faturaService.analisarCompras(linhas);
        List<Compra> saved = faturaService.salvar(compras);

        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Apaga todas as compras e faturas",
            description = "Remove todos os dados salvos no banco.")
    @ApiResponse(responseCode = "204", description = "Dados apagados (sem corpo na resposta)")
    @DeleteMapping("/faturas/deleta")
    public ResponseEntity<Void> deleteAll() {
        faturaService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}