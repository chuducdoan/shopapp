package com.product.shoppapp.controllers;

import com.product.shoppapp.dtos.CategoryDTO;
import com.product.shoppapp.models.Category;
import com.product.shoppapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Category category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(category);
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoriesById(@PathVariable("id") String id) {
        Category category = categoryService.getCategoryById(Long.parseLong(id));
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable("id") String id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        Category category = categoryService.updateCategory(Long.parseLong(id), categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") String id) {
        categoryService.deleteCategory(Long.parseLong(id));
        return ResponseEntity.ok("delete category with id: " + id + " successfully");
    }
}
