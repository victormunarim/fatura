package com.example.fatura.exception;

// lançada quando o cliente manda algo inválido (400)
public class RequisicaoInvalidaException extends RuntimeException {
    public RequisicaoInvalidaException(String message) {
        super(message);
    }
}