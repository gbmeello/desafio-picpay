package br.com.gabrielmello.desafio_picpay.transaction.repository;


import br.com.gabrielmello.desafio_picpay.transaction.domain.Transaction;
import org.springframework.data.repository.ListCrudRepository;

public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
}
