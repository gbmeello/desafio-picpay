package br.com.gabrielmello.desafio_picpay.wallet.repository;

import br.com.gabrielmello.desafio_picpay.wallet.domain.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
