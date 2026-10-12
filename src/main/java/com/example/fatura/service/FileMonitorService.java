package com.example.fatura.service;

import com.example.fatura.event.ArquivoDetectadoEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileMonitorService {

    private static final String CAMINHO = "fatura";
    private final ApplicationEventPublisher eventPublisher;
    private Thread monitorThread;
    private volatile boolean running = true;

    public FileMonitorService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() {
        Path diretorio = Paths.get(CAMINHO);
        try {
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório base 'fatura': " + e.getMessage());
        }

        iniciarMonitoramento(diretorio);
    }

    @PreDestroy
    public void destroy() {
        running = false;
        if (monitorThread != null) {
            monitorThread.interrupt();
        }
    }

    public List<Path> getArquivoExistente() {
        Path diretorio = Paths.get(CAMINHO);
        if (!Files.exists(diretorio)) return new ArrayList<>();

        try (Stream<Path> stream = Files.list(diretorio)) {
            return stream.filter(Files::isRegularFile).toList();
        } catch (IOException e) {
            System.err.println("Erro ao listar arquivos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void iniciarMonitoramento(Path diretorio) {
        monitorThread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                diretorio.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

                IO.println("Monitoramento de diretório iniciado em: " + diretorio.toAbsolutePath());

                while (running) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                            Path name = (Path) event.context();
                            Path arquivoCompleto = diretorio.resolve(name);

                            IO.println("Novo arquivo detectado pelo monitor: " + name);
                            
                            aguardarEscritaCompleta(arquivoCompleto);

                            eventPublisher.publishEvent(new ArquivoDetectadoEvent(this, arquivoCompleto));
                        }
                    }
                    key.reset();
                }
            } catch (IOException e) {
                System.err.println("Erro no monitoramento: " + e.getMessage());
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    private void aguardarEscritaCompleta(Path arquivo) {
        try {
            long tamanhoAnterior = -1;
            while (true) {
                long tamanhoAtual = Files.size(arquivo);
                if (tamanhoAnterior == tamanhoAtual && tamanhoAtual > 0) {
                    break;
                }
                tamanhoAnterior = tamanhoAtual;
            }
        } catch (IOException e) {
            System.err.println("Erro ao aguardar gravação do arquivo: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
