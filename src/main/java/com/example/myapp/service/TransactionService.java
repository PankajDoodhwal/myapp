package com.example.myapp.service;

import com.example.myapp.common.TransactionType;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.CreateTransactionRequest;
import com.example.myapp.exception.InvalidDataException;
import com.example.myapp.model.*;
import com.example.myapp.repository.*;
import com.example.myapp.utils.ProjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ProjectUtils projectUtils;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScopeRepository scopeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private static final PrettyLogger logger = PrettyLogger.getLogger(TransactionService.class);

    public Transaction createNewTransaction(CreateTransactionRequest createTransactionRequest) {
        logger.info("Creating new transaction");

        Transaction transaction = new Transaction();
        setAdditionalData(transaction, createTransactionRequest);

        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("Saved the transaction in the database.");
        return savedTransaction;
    }

    public void setAdditionalData (Transaction transaction, CreateTransactionRequest createTransactionRequest){
        logger.info("setting Additional data to the transaction");
        User user = projectUtils.getUserFromToken();
        transaction.setUser(user);
        Scope scope = scopeRepository.findByScopeIdAndUserId(createTransactionRequest.scopeId(), user.getId());
        transaction.setScope(scope);
        transaction.setCategories(getAllCategories(transaction, createTransactionRequest.categoryIds()));
        Account account = accountRepository.getAccountByAccNumberAndUser(createTransactionRequest.accountNo(), user.getId());
        transaction.setBankAccount(account);
        transaction.setTxnDate(createTransactionRequest.txnDate());
        transaction.setNote(createTransactionRequest.note());

        if(StringUtils.equalsIgnoreCase(createTransactionRequest.txnType(), "credit")){
            transaction.setTxnType(TransactionType.CREDIT);
        } else if (StringUtils.equalsIgnoreCase(createTransactionRequest.txnType(), "debit")){
            transaction.setTxnType(TransactionType.DEBIT);
        } else {
            throw new RuntimeException("Invalid transaction type while creating transaction");
        }

        setBalanceForTransaction(transaction, createTransactionRequest);
    }

    private void setBalanceForTransaction(Transaction transaction, CreateTransactionRequest createTransactionRequest) {
        logger.info("Validating amount for the transaction");
        Account account = transaction.getBankAccount();

        if(transaction.getTxnType().equals(TransactionType.DEBIT)) {
            if(account.getBalance() < createTransactionRequest.amount()){
                throw new InvalidDataException("Insufficient Balance");
            } else {
                transaction.setAmount(createTransactionRequest.amount());
                account.setBalance(projectUtils.subAndRound(account.getBalance(), createTransactionRequest.amount()).doubleValue());
            }
        } else {
            transaction.setAmount(createTransactionRequest.amount());
            account.setBalance(projectUtils.addAndRound(account.getBalance(), createTransactionRequest.amount()).doubleValue());
        }

        logger.info("saving account data to database.");
        account.setModifiedDate(LocalDateTime.now());
        accountRepository.save(account);
    }

    /**
     * getting all the categories from the database for a particular user and
     * then checks the given data in the list.
     */
    public Set<Category> getAllCategories(Transaction transaction, Set<Long> categoryList) {
        logger.info("getting all the categories from db");
        List<Category> allCategoriesOfUser = categoryRepository.getAllCategoriesByUserAndScopeId(transaction.getScope().getId());

        Set<Category> fetchedCategories = new HashSet<>();

        logger.info("Checking all the categories and extracting them in set");
        for(Long x: categoryList) {
            for(Category category: allCategoriesOfUser) {
                if(Objects.equals(category.getId(), x)){
                    fetchedCategories.add(category);
                    break;
                }
            }
        }

        if(fetchedCategories.size() != categoryList.size()) {
            throw new InvalidDataException("Invalid entered categories");
        }

        return fetchedCategories;
    }

    public List<Transaction> fetchAllTheTransactions() {
        User user = projectUtils.getUserFromToken();
        logger.info("Getting all the transactions for the users");
        List<Transaction> transactionList = transactionRepository.getAllTransactionByUser(user.getId());
        logger.info("Fetched all the transactions successfully.");
        return transactionList;
    }
}
