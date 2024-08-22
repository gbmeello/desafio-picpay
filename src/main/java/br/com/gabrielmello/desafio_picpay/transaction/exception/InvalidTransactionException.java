package br.com.gabrielmello.desafio_picpay.transaction.exception;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message){
        super(message);

    }
}
