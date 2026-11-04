package com.example.fatura.controller;

import com.example.fatura.dto.CompraDTO;
import com.example.fatura.model.Compra;
import com.example.fatura.service.FaturaService;
import com.example.fatura.service.WsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Faturas", description = "Processamento e exclusão das faturas")
@RestController
public class FaturaController {

    private final FaturaService faturaService;
    private final WsHandler wsHandler;

    public FaturaController(FaturaService faturaService, WsHandler wsHandler) {
        this.faturaService = faturaService;
        this.wsHandler = wsHandler;
    }

    @Operation(summary = "Envia o PDF e salva as compras",
            description = "Recebe o PDF (multipart, campo 'arquivo'), extrai os lançamentos, cria uma Fatura e salva as compras.")
    @ApiResponse(responseCode = "201", description = "Fatura processada; devolve as compras criadas")
    @ApiResponse(responseCode = "500", description = "Erro ao ler o PDF",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Erro ao ler ou processar arquivo: ...")))
    @PostMapping(value = "/faturas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CompraDTO>> processarFatura(
            @RequestParam("arquivo") MultipartFile arquivo) throws IOException {

        List<String> linhas = faturaService.extrairLinhas(arquivo.getInputStream());
        List<Compra> compras = faturaService.analisarCompras(linhas);
        List<Compra> saved = faturaService.salvar(compras);

        wsHandler.notificarTodos("Upload processado: " + saved.size() + " compras salvas");

        // 201 Created: um recurso foi criado
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved.stream().map(CompraDTO::de).toList());
    }

    @Operation(summary = "Apaga todas as compras e faturas",
            description = "Remove todos os dados salvos no banco.")
    @ApiResponse(responseCode = "204", description = "Dados apagados (sem corpo na resposta)")
    @DeleteMapping("/faturas")
    public ResponseEntity<Void> deleteAll() {
        faturaService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}