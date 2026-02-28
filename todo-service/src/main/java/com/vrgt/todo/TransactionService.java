package com.vrgt.todo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new transaction and updates the associated account balance
     */
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getAccountId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID is required");
        }

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        // Save transaction first
        Transaction saved = transactionRepository.save(transaction);

        // Update account balance based on transaction type
        updateAccountBalance(account, transaction.getAmount(), transaction.getType(), true);
        accountRepository.save(account);

        return saved;
    }

    /**
     * Updates an existing transaction and recalculates the account balance
     */
    public Transaction updateTransaction(Long id, Transaction updates) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        Account account = accountRepository.findById(existingTransaction.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        // Check if amount or type is being updated
        boolean amountChanged = updates.getAmount() != null && !updates.getAmount().equals(existingTransaction.getAmount());
        boolean typeChanged = updates.getType() != null && !updates.getType().equals(existingTransaction.getType());

        if (amountChanged || typeChanged) {
            // Reverse the old transaction impact
            updateAccountBalance(account, existingTransaction.getAmount(), existingTransaction.getType(), false);

            // Apply the new transaction impact
            Double newAmount = updates.getAmount() != null ? updates.getAmount() : existingTransaction.getAmount();
            TransactionType newType = updates.getType() != null ? updates.getType() : existingTransaction.getType();
            updateAccountBalance(account, newAmount, newType, true);

            accountRepository.save(account);
        }

        // Update transaction fields
        if (updates.getAccountId() != null) existingTransaction.setAccountId(updates.getAccountId());
        if (updates.getAmount() != null) existingTransaction.setAmount(updates.getAmount());
        if (updates.getDate() != null) existingTransaction.setDate(updates.getDate());
        if (updates.getDescription() != null) existingTransaction.setDescription(updates.getDescription());
        if (updates.getType() != null) existingTransaction.setType(updates.getType());

        return transactionRepository.save(existingTransaction);
    }

    /**
     * Deletes a transaction and reverses its impact on the account balance
     */
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        // Reverse the transaction impact on the account balance
        updateAccountBalance(account, transaction.getAmount(), transaction.getType(), false);
        accountRepository.save(account);

        transactionRepository.deleteById(id);
    }

    /**
     * Helper method to update account balance based on transaction type
     * @param account The account to update
     * @param amount The transaction amount
     * @param type The transaction type (CREDIT or DEBIT)
     * @param isAdding true if adding the transaction impact, false if reversing it
     */
    private void updateAccountBalance(Account account, Double amount, TransactionType type, boolean isAdding) {
        if (amount == null || type == null) {
            return;
        }

        double currentBalance = account.getBalance() != null ? account.getBalance() : 0;
        double adjustedAmount = isAdding ? amount : -amount;

        if (type == TransactionType.CREDIT) {
            account.setBalance(currentBalance + adjustedAmount);
        } else if (type == TransactionType.DEBIT) {
            account.setBalance(currentBalance - adjustedAmount);
        }
    }

    /**
     * Gets all transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Gets a transaction by ID
     */
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    /**
     * Gets all transactions for a specific account
     */
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
