package com.example.fatura.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.nio.file.Path;

@Getter
public class ArquivoDetectadoEvent extends ApplicationEvent {
    
    private final Path arquivo;

    public ArquivoDetectadoEvent(Object source, Path arquivo) {
        super(source);
        this.arquivo = arquivo;
    }

}
