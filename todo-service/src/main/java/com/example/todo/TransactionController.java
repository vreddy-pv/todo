package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TransactionController {

    private final TransactionRepository repository;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<Transaction> all() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody Transaction transaction) {
        // Save transaction first
        Transaction saved = repository.save(transaction);
        // Update associated account balance based on transaction type
        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        double amount = transaction.getAmount() != null ? transaction.getAmount() : 0;
        if (transaction.getType() == TransactionType.CREDIT) {
            account.setBalance((account.getBalance() != null ? account.getBalance() : 0) + amount);
        } else if (transaction.getType() == TransactionType.DEBIT) {
            account.setBalance((account.getBalance() != null ? account.getBalance() : 0) - amount);
        }
        accountRepository.save(account);
        return saved;
    }

    @PatchMapping("/{id}")
    public Transaction update(@PathVariable Long id, @RequestBody Transaction updates) {
        return repository.findById(id)
                .map(transaction -> {
                    if (updates.getAccountId() != null) transaction.setAccountId(updates.getAccountId());
                    if (updates.getAmount() != null) transaction.setAmount(updates.getAmount());
                    if (updates.getDate() != null) transaction.setDate(updates.getDate());
                    if (updates.getDescription() != null) transaction.setDescription(updates.getDescription());
                    if (updates.getType() != null) transaction.setType(updates.getType());
                    return repository.save(transaction);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }
        repository.deleteById(id);
    }
}
