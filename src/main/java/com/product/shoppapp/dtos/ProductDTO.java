package com.product.shoppapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;
    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 1000000, message = "Price must be less than or equal to 1000000")
    private Float price;

    private String thumbnail;

    @JsonProperty("category_id")
    private String categoryId;

    private List<MultipartFile> files;
}
