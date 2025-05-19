package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.CreateCategoryRequest;
import com.example.myapp.exception.DuplicateException;
import com.example.myapp.exception.RestrictedOperation;
import com.example.myapp.model.Category;
import com.example.myapp.model.Scope;
import com.example.myapp.model.User;
import com.example.myapp.repository.CategoryRepository;
import com.example.myapp.repository.ScopeRepository;
import com.example.myapp.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProjectUtils projectUtils;
    @Autowired
    ScopeRepository scopeRepository;

    private static final PrettyLogger logger = PrettyLogger.getLogger(CategoryService.class);


    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
//        checking scope with scopeId and userid
        logger.info("checking scope with scopeId and userid");
        User userFromToken = projectUtils.getUserFromToken();

        Scope scopeFromRequest = scopeRepository.findByScopeIdAndUserId(createCategoryRequest.scope_id(), userFromToken.getId());

        if(scopeFromRequest == null){
            throw new RestrictedOperation("You are not allowed to add category in this scope");
        }

//        checking categories before creating new category to avoid duplicate
        logger.info("checking categories before creating new category to avoid duplicate");
        List<Category> categoriesList = categoryRepository.findByScopeIdAndName(scopeFromRequest.getId(), createCategoryRequest.name());

        if(!categoriesList.isEmpty()) {
            throw new DuplicateException(createCategoryRequest.name() +" is already created in " + scopeFromRequest.getScopeName() + " for you");
        }

//        Creating new category
        logger.info("Creating new category");
        Category category = new Category();
        category.setName(createCategoryRequest.name());
        category.setScope(scopeFromRequest);

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
//        getting all the categories from database;
        User user = projectUtils.getUserFromToken();
        logger.info("Getting all the Categories for the user");
        List<Category> categoryList = categoryRepository.getAllCategoriesByUser(user.getId());
        logger.info("Fetched all the categories successfully successfully.");
        return categoryList;
    }
}
