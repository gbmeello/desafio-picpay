package br.com.gabrielmello.desafio_picpay.transaction.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("TRANSACTIONS")
public record Transaction(@Id Long id, Long payer, Long payee, BigDecimal value, LocalDateTime createAt) {

    public Transaction{
        value = value.setScale(2);
    }

}
