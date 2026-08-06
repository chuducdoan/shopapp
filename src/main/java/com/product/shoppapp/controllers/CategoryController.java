package com.product.shoppapp.controllers;

import com.product.shoppapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @GetMapping("")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok("All categories");
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
       if (bindingResult.hasErrors()) {
           List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
           return ResponseEntity.badRequest().body(errorMessages);
       }
        return ResponseEntity.ok("Create category " + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") String id) {
        return ResponseEntity.ok("update category with id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") String id) {
        return ResponseEntity.ok("delete category with id: " + id);
    }
}
