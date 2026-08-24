package com.example.fatura.service;

import com.example.fatura.model.Compra;
import com.example.fatura.repository.CompraRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class FaturaService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final CompraRepository compraRepository;

    public FaturaService(CompraRepository lancamentoRepository) {
        this.compraRepository = lancamentoRepository;
    }

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

    public List<Compra> analisarCompras(List<String> linhas) {
        List<Compra> compras = new ArrayList<>();

        var padrao = Pattern.compile("^(\\d{2}/\\d{2}/\\d{4})\\s+(.+)$");
        var valorPattern = Pattern.compile("(?<![\\d.])-?\\d+(?:\\.\\d{3})*,\\d{2}(?!\\d)");

        for (String linha : linhas) {
            var matcher = padrao.matcher(linha);
            if (!matcher.find()) continue;

            String dataTexto = matcher.group(1);
            String restante = matcher.group(2);

            if (restante.contains("SALDO DO DIA")) continue;

            var valorMatcher = valorPattern.matcher(restante);
            if (!valorMatcher.find()) continue;

            String valorTexto = valorMatcher.group().replace(".", "").replace(",", ".");
            String descricao = restante.substring(0, valorMatcher.start()).trim();

            Compra lancamento = new Compra();
            lancamento.setData(LocalDate.parse(dataTexto, FORMATO_DATA));
            lancamento.setDescricao(descricao);
            lancamento.setValor(new BigDecimal(valorTexto));
            lancamento.setCategoria(classificar(descricao));

            compras.add(lancamento);
        }

        return compras;
    }

    private String classificar(String descricao) {
        if (descricao.startsWith("PIX")) return "Pix";
        if (descricao.startsWith("TED")) return "Transferência";
        if (descricao.startsWith("TAR")) return "Tarifa";
        if (descricao.startsWith("REND")) return "Rendimento";
        if (descricao.startsWith("FATURA PAGA")) return "Pagamento de fatura";
        return "Outros";
    }

    public List<Compra> salvar(List<Compra> lancamentos) {
        return compraRepository.saveAll(lancamentos);
    }
}