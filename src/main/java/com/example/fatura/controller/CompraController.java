package com.example.fatura.controller;

import com.example.fatura.dto.CategoriaGasto;
import com.example.fatura.dto.CompraDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(summary = "Lista as compras",
            description = "Devolve as compras salvas. 'categoria' filtra; 'ordem' (asc ou desc) ordena por valor. Se vier categoria, ela tem prioridade. Lista vazia devolve [].")
    @ApiResponse(responseCode = "200", description = "Lista de compras (pode ser vazia)")
    @GetMapping("/compras")
    public ResponseEntity<List<CompraDTO>> listar(
            @Parameter(description = "Filtra pela categoria", example = "Pix")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Ordena pelo valor: asc ou desc", example = "desc")
            @RequestParam(required = false) String ordem) {

        List<Compra> compras;
        if (categoria != null) {
            compras = compraService.findByCategoria(categoria);
        } else if ("asc".equals(ordem)) {
            compras = compraService.getAllOrdenadoCrescente();
        } else if ("desc".equals(ordem)) {
            compras = compraService.getAllOrdenadoDescendente();
        } else {
            compras = compraService.getAll();
        }
        return ResponseEntity.ok(compras.stream().map(CompraDTO::de).toList());
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