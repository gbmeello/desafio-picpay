package br.com.gabrielmello.desafio_picpay.transaction.service;

import java.util.List;

import br.com.gabrielmello.desafio_picpay.authorization.service.AuthorizerService;
import br.com.gabrielmello.desafio_picpay.notification.service.NotificationService;
import br.com.gabrielmello.desafio_picpay.transaction.domain.Transaction;
import br.com.gabrielmello.desafio_picpay.transaction.exception.InvalidTransactionException;
import br.com.gabrielmello.desafio_picpay.transaction.repository.TransactionRepository;
import br.com.gabrielmello.desafio_picpay.wallet.domain.WalletType;
import br.com.gabrielmello.desafio_picpay.wallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository,
                              WalletRepository walletRepository, AuthorizerService authorizerService,
                              NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        validate(transaction);

        var newTransaction = transactionRepository.save(transaction);

        var walletPayer = walletRepository.findById(transaction.payer()).get();
        var walletPayee = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(walletPayer.debit(transaction.value()));
        walletRepository.save(walletPayee.credit(transaction.value()));

        authorizerService.authorize(transaction);
        notificationService.notify(newTransaction);

        return newTransaction;
    }

    /*
     * A transaction is valid if:
     * - the payer is a common wallet
     * - the payer has enough balance
     * - the payer is not the payee
     */
    private void validate(Transaction transaction) {
        LOGGER.info("validating transaction {}...", transaction);

        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(
                                payer -> payer.type() == WalletType.COMUM.getValue() &&
                                        payer.balance().compareTo(transaction.value()) >= 0 &&
                                        !payer.id().equals(transaction.payee()) ? true : null)
                        .orElseThrow(() -> new InvalidTransactionException(
                                "Invalid transaction - " + transaction)))
                .orElseThrow(() -> new InvalidTransactionException(
                        "Invalid transaction - " + transaction));
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}