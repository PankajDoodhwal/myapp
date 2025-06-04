package com.example.myapp.service;

import com.example.myapp.common.TransactionType;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.ScopeRequest;
import com.example.myapp.dto.ScopeResponse;
import com.example.myapp.exception.DuplicateException;
import com.example.myapp.exception.InvalidDataException;
import com.example.myapp.model.Scope;
import com.example.myapp.model.User;
import com.example.myapp.repository.ScopeRepository;
import com.example.myapp.utils.ProjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScopeService {
    @Autowired
    ScopeRepository scopeRepository;

    @Autowired
    ProjectUtils projectUtils;

    private static final PrettyLogger logger = PrettyLogger.getLogger(ScopeService.class);

    public Scope createScope(ScopeRequest scopeRequest) {

        User user = projectUtils.getUserFromToken();
        String scopeName = scopeRequest.name();

        Scope existingScope = scopeRepository.findScopeByUserAndName(user.getId(), scopeName);
        if(existingScope != null){
            throw new DuplicateException("The scope name " + scopeName + " is already present for you!");
        }

        TransactionType transactionType;
        if(StringUtils.equalsIgnoreCase(scopeRequest.txntype(), "credit")) {
            transactionType = TransactionType.CREDIT;
        } else if (StringUtils.equalsIgnoreCase(scopeRequest.txntype(), "debit")) {
            transactionType = TransactionType.DEBIT;
        } else {
            throw new InvalidDataException(scopeRequest.txntype() + "is Invalid transaction Type Valid transaction type is only debit and credit");
        }

        Scope scope = new Scope();
        scope.setScopeName(scopeName);
        scope.setTxnType(transactionType);
        scope.setUser(user);

        return scopeRepository.save(scope);

    }

    public List<ScopeResponse> getAllScopeByUser() {
        User user = projectUtils.getUserFromToken();
        if (user == null) {
            return List.of(); // Or handle as an error
        }

        // Assuming your ScopeRepository has a method like findByUser or findByUserId
        List<Scope> scopes = scopeRepository.getAllScopeByUser(user.getId());
        // OR scopeRepository.findByUser(user);

        // Map the list of Scope entities to a list of ScopeResponse DTOs
        return scopes.stream()
                .map(scope -> new ScopeResponse(
                        scope.getId(),
                        scope.getScopeName(),
                        scope.getTxnType() != null ? scope.getTxnType().name() : null // Convert enum to String
                ))
                .collect(Collectors.toList());
    }
}
