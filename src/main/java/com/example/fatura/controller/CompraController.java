package com.example.fatura.controller;

import com.example.fatura.dto.CategoriaGasto;
import com.example.fatura.model.Compra;
import com.example.fatura.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @Tag agrupa os endpoints desta classe sob o nome "Compras" no Swagger
@Tag(name = "Compras", description = "Consulta, ordenação, filtro e análise das compras salvas")
@RestController
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // @Operation descreve o endpoint; cada @ApiResponse descreve um código de resposta possível
    @Operation(summary = "Lista todas as compras",
            description = "Devolve todas as compras salvas no banco.")
    @ApiResponse(responseCode = "200", description = "Lista de compras")
    @ApiResponse(responseCode = "404", description = "Não há nenhuma compra salva",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Nenhuma compra encontrada")))
    @GetMapping("/compras")
    public ResponseEntity<List<Compra>> getCompras() {
        List<Compra> compras = compraService.getAll();
        return ResponseEntity.ok(compras);
    }

    @Operation(summary = "Lista as compras da menor para a maior",
            description = "Ordena pelo valor, em ordem crescente (os gastos, que são negativos, vêm primeiro).")
    @ApiResponse(responseCode = "200", description = "Lista de compras ordenada por valor crescente")
    @ApiResponse(responseCode = "404", description = "Não há nenhuma compra salva",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Nenhuma compra encontrada")))
    @GetMapping("/compras/ordenado/crescente")
    public ResponseEntity<List<Compra>> getComprasOrdenadoCrescente() {
        List<Compra> compras = compraService.getAllOrdenadoCrescente();
        return ResponseEntity.ok(compras);
    }

    @Operation(summary = "Lista as compras da maior para a menor",
            description = "Ordena pelo valor, em ordem decrescente.")
    @ApiResponse(responseCode = "200", description = "Lista de compras ordenada por valor decrescente")
    @ApiResponse(responseCode = "404", description = "Não há nenhuma compra salva",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Nenhuma compra encontrada")))
    @GetMapping("/compras/ordenado/decrescente")
    public ResponseEntity<List<Compra>> getComprasOrdenadoDescendente() {
        List<Compra> compras = compraService.getAllOrdenadoDescendente();
        return ResponseEntity.ok(compras);
    }

    @Operation(summary = "Filtra as compras por categoria",
            description = "Devolve só as compras da categoria informada.")
    @ApiResponse(responseCode = "200", description = "Compras da categoria")
    @ApiResponse(responseCode = "404", description = "Nenhuma compra na categoria informada",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Nenhuma compra encontrada para a categoria: Viagem")))
    @GetMapping("/compras/categoria/{categoria}")
    public ResponseEntity<List<Compra>> getComprasPorCategoria(
            @Parameter(description = "Nome da categoria", example = "Pix")
            @PathVariable String categoria) {
        List<Compra> compras = compraService.findByCategoria(categoria);
        return ResponseEntity.ok(compras);
    }

    @Operation(summary = "Distribuição dos gastos por categoria",
            description = "Soma os gastos (compras de valor negativo) de cada categoria e informa o total e o percentual sobre o total de gastos, do maior para o menor. Entradas (valor positivo) são ignoradas.")
    @ApiResponse(responseCode = "200", description = "Categorias com total e percentual")
    @ApiResponse(responseCode = "404", description = "Não há nenhum gasto (banco vazio ou só entradas)",
            content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Nenhum gasto encontrado")))
    @GetMapping("/compras/distribuicao")
    public ResponseEntity<List<CategoriaGasto>> getDistribuicaoGastos(){
        List<CategoriaGasto> distribuicao = compraService.getDistribuicaoGastos();
        return ResponseEntity.ok(distribuicao);
    }
}