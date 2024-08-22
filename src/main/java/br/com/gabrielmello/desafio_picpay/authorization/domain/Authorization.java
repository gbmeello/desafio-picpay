package br.com.gabrielmello.desafio_picpay.authorization.domain;

public record Authorization(String message) {

    public boolean isAuthorized(){
        return message.equals("Autorizado");
    }
}
