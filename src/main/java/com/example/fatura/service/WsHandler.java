package com.example.fatura.service;

import com.example.fatura.event.ArquivoDetectadoEvent;
import com.example.fatura.model.Compra;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WsHandler extends TextWebSocketHandler {

    private final FileMonitorService fileMonitorService;
    private final FaturaService faturaService;
    private final ObjectMapper objectMapper;
    
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    public WsHandler(FileMonitorService fileMonitorService, FaturaService faturaService, ObjectMapper objectMapper) {
        super();
        this.fileMonitorService = fileMonitorService;
        this.faturaService = faturaService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        IO.println("Cliente conectado: " + session.getId());
        sessions.add(session);

        List<Path> arquivos = fileMonitorService.getArquivoExistente();

        if (arquivos.isEmpty()) {
            IO.println("Nenhum arquivo encontrado no momento. Aguardando novo arquivo...");
        } else {
            for (Path arquivo : arquivos) {
                IO.println("Arquivo já existente encontrado: " + arquivo.getFileName());
                processarEEnviarCompras(session, arquivo);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        IO.println("Cliente desconectado: " + session.getId());
        sessions.remove(session);
    }

    @EventListener
    public void handleArquivoDetectado(ArquivoDetectadoEvent event) {
        Path arquivo = event.getArquivo();
        IO.println("WsHandler notificado sobre o novo arquivo: " + arquivo.getFileName());
        
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                processarEEnviarCompras(session, arquivo);
            }
        }
    }

    private void processarEEnviarCompras(WebSocketSession session, Path arquivo) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage("Processando arquivo: " + arquivo.getFileName() + "..."));

                List<String> linhas = faturaService.extrairLinhas(arquivo.toString());
                List<Compra> compras = faturaService.analisarCompras(linhas);

                String jsonCompras = objectMapper.writeValueAsString(compras);

                session.sendMessage(new TextMessage("COMPRAS_JSON:" + jsonCompras));
            }
        } catch (IOException e) {
            System.err.println("Erro ao processar e enviar compras para sessão " + session.getId() + ": " + e.getMessage());
        }
    }
}
