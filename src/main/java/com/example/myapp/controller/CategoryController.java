package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.CategoryResponse;
import com.example.myapp.dto.CreateCategoryRequest;
import com.example.myapp.model.Category;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(CategoryController.class);

    @SuppressWarnings("unused")
    @PostMapping("/create-category")
    public ApiResponse<Category> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Creating new Category");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.CATEGORY);
        Category newCategory = categoryService.createCategory(createCategoryRequest);
        logger.info("Created new Category:- " + newCategory.toString());
        ctx.put(MofConstants.CREATED_ENTITY_ID, newCategory.getId());
        return ApiResponse.success(newCategory, "Category Created Successfully", ctx.getTraceId());
    }

    @SuppressWarnings("unused")
    @GetMapping("/get-all-categories")
    public ApiResponse<List<CategoryResponse>> getAllCategories(){
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Getting all categories:- ");
        List<CategoryResponse> allCategories = categoryService.getAllCategories();
        logger.info("Fetched all the Categories:- ");
        return ApiResponse.success(allCategories, "Fetched all the categories successfully.", ctx.getTraceId());
    }
}
