package com.example.myapp.service;

import com.example.myapp.common.AccountType;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.CreateAccountRequest;
import com.example.myapp.exception.DuplicateException;
import com.example.myapp.exception.InvalidDataException;
import com.example.myapp.model.Account;
import com.example.myapp.model.User;
import com.example.myapp.repository.AccountRepository;
import com.example.myapp.utils.ProjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProjectUtils projectUtils;

    private static final PrettyLogger logger = PrettyLogger.getLogger(RequestLogService.class);


    public Account createAccount(CreateAccountRequest createAccountRequest) {
        String accountNumber = createAccountRequest.accountNumber();
        Account tempAccount;

        User user = projectUtils.getUserFromToken();

        tempAccount = accountRepository.getAccountByAccNumberAndUser(accountNumber, user.getId());
        if(tempAccount != null) {
            throw new DuplicateException("This account number " + accountNumber + " is already stored for you");
        }

        tempAccount = new Account();
        tempAccount.setBankName(createAccountRequest.bankName());
        tempAccount.setAccountNumber(accountNumber);
        tempAccount.setIfsc(createAccountRequest.ifsc());
        tempAccount.setUser(user);
        if(StringUtils.equalsIgnoreCase(createAccountRequest.accountType(), "saving")){
            tempAccount.setAccountType(AccountType.SAVING);
        } else if(StringUtils.equalsIgnoreCase(createAccountRequest.accountType(), "current")){
            tempAccount.setAccountType(AccountType.CURRENT);
        } else if(StringUtils.equalsIgnoreCase(createAccountRequest.accountType(), "business")){
            tempAccount.setAccountType(AccountType.BUSINESS);
        } else if(StringUtils.equalsIgnoreCase(createAccountRequest.accountType(), "loan")){
            tempAccount.setAccountType(AccountType.LOAN);
        } else {
            throw new InvalidDataException("The account you entered " + createAccountRequest.accountType() + ". valid account types are saving, current, business and loan");
        }

        tempAccount.setCreatedDate(LocalDateTime.now());
        tempAccount.setModifiedDate(LocalDateTime.now());

        if(createAccountRequest.balance() != null) {
            tempAccount.setBalance(createAccountRequest.balance());
        } else {
            tempAccount.setBalance(0.00);
        }

        return accountRepository.save(tempAccount);
    }

    public List<Account> getAllAccountByUser(){
        User user = projectUtils.getUserFromToken();
        logger.info("Getting all the accounts for the users");
        List<Account> accountList = accountRepository.getAllAccountByUserId(user.getId());
        logger.info("Fetched all the accounts successfully.");
        return accountList;
    }
}
