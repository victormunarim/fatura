package com.example.fatura.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        boolean dentroSecao = false;
        var padrao = Pattern.compile("(\\d{2}/\\d{2})\\s+(.+?)\\s+(\\d+[.,]\\d{2})(?:\\s+(.*))?");
        for (String linha : linhas) {
            if (linha.contains("Lançamentos: compras e saques")) {
                dentroSecao = true;
                continue;
            }

            if (linha.contains("Lançamentos produtos e serviços")) {
                dentroSecao = false;
                continue;
            }

            if (!dentroSecao) {
                continue;
            }

            var matcher = padrao.matcher(linha);
            if (matcher.find()) {
                String data = matcher.group(1);
                String descricao = matcher.group(2).trim();
                String valor = matcher.group(3).replace(',', '.');
                compras.add(Map.of(
                        "Data", data,
                        "Descrição", descricao,
                        "Valor", valor
                ));
            }
        }

        return compras;
    }
}