package com.product.shoppapp.controllers;

import com.product.shoppapp.dtos.ProductDTO;
import com.product.shoppapp.dtos.ProductImageDTO;
import com.product.shoppapp.models.Product;
import com.product.shoppapp.models.ProductImage;
import com.product.shoppapp.responses.ListProductResponse;
import com.product.shoppapp.responses.ProductResponse;
import com.product.shoppapp.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<ListProductResponse> getAllProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", value = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit,
//                Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        Page<ProductResponse> products = productService.getAllProducts(keyword, categoryId, pageRequest);
        int totalPages = products.getTotalPages();
        List<ProductResponse> content = products.getContent();
        ListProductResponse listProductResponse = ListProductResponse.builder()
                .products(content)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(listProductResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Product existingProduct = productService.getProductById(id);
            ProductResponse productResponse = ProductResponse.fromProduct(existingProduct);
            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createProducts(@Valid @RequestBody ProductDTO productDTO,
                                            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files, @PathVariable("id") Long id) {
       try {
           files = files == null ? new ArrayList<MultipartFile>() : files;
           if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
               return ResponseEntity.badRequest().body("You can only upload up to " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " images");
           }
           Product existingProduct = productService.getProductById(id);
           List<ProductImage> productImages = new ArrayList<>();
           for (MultipartFile file : files) {
               if (file.getSize() == 0) {
                   continue;
               }
               if (file.getSize() > 10 * 1024 * 1024) {
                   return ResponseEntity.badRequest().body("File size exceeds 10MB");
               }
               String filename = storeFile(file);
               ProductImage productImage = productService.createProductImage(existingProduct.getId(), ProductImageDTO.builder()
                       .imageUrl(filename)
                       .build());
               productImages.add(productImage);
           }
           return ResponseEntity.ok().body(productImages);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error: " + e.getMessage());
       }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok("update product with id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product with id: " + id + " successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            } else {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/NotFound.jpg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid file type");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestParam("ids") String ids) {
       try {
           List<Long> productIds = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
           List<Product> products = productService.findProductsByIds(productIds);
           return ResponseEntity.ok(products);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(null);
       }
    }

}
