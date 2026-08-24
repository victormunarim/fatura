package com.example.fatura.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FaturaService {

    public List<String> extrairLinhas(String caminhoPdf) throws IOException {
        try (PDDocument documento = PDDocument.load(new File(caminhoPdf))) {
            PDFTextStripper extrator = new PDFTextStripper();
            String texto = extrator.getText(documento);
            List<String> linhas = new ArrayList<>();

            for (String linha : texto.split("\\r?\\n")) {
                String limpa = linha.trim();
                if (!limpa.isEmpty()) {
                    linhas.add(limpa);
                }
            }

            return linhas;
        }
    }

    public List<Map<String, String>> analisarCompras(List<String> linhas) {
        List<Map<String, String>> compras = new ArrayList<>();

        var padrao = Pattern.compile(
                "^(\\d{2}/\\d{2}/\\d{4})\\s+(.+)$"
        );

        var valorPattern = Pattern.compile(
                "(?<![\\d.])-?\\d+(?:\\.\\d{3})*,\\d{2}(?!\\d)"
        );

        for (String linha : linhas) {
            var matcher = padrao.matcher(linha);

            if (!matcher.find()) {
                continue;
            }

            String data = matcher.group(1);
            String restante = matcher.group(2);

            if (restante.contains("SALDO DO DIA")) {
                continue;
            }

            var valorMatcher = valorPattern.matcher(restante);

            if (!valorMatcher.find()) {
                continue;
            }

            String valor = valorMatcher.group();
            String descricao = restante.substring(0, valorMatcher.start()).trim();

            valor = valor.replace(".", "").replace(",", ".");

            compras.add(Map.of(
                    "Data", data,
                    "Descrição", descricao,
                    "Valor", valor
            ));
        }

        return compras;
    }

    public List<Map<String, String>> analisarComprasOrdenado(List<String> linhas) {
        List<Map<String, String>> compras = analisarCompras(linhas);
        return compras.stream()
                .sorted(Comparator.comparing(item -> Double.parseDouble(item.get("Valor"))))
                .collect(Collectors.toList());
    }
}