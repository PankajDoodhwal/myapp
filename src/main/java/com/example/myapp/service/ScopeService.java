package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.ScopeRequest;
import com.example.myapp.model.Scope;
import com.example.myapp.model.User;
import com.example.myapp.repository.ScopeRepository;
import com.example.myapp.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScopeService {
    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private ProjectUtils projectUtils;

    private static final PrettyLogger logger = PrettyLogger.getLogger(ScopeService.class);

    public Scope createScope(ScopeRequest scopeRequest) {
        Scope scope = new Scope();

        scope.setScopeName(scopeRequest.name());
        User user = projectUtils.getUserFromToken();
        scope.setUser(user);

        return scopeRepository.save(scope);

    }
}
