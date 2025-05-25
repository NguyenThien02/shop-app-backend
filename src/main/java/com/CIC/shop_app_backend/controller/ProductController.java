package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.PaginationRequest;
import com.CIC.shop_app_backend.dtos.ProductDTO;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.responses.ListProductResponse;
import com.CIC.shop_app_backend.responses.ProductResponse;
import com.CIC.shop_app_backend.services.Impl.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping("/by-category")
    public ResponseEntity<?> getProductByCategory(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "0", name = "category_id") Long category_id
    ){
        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    limit,
                    Sort.by("productId").ascending()
            );
            Page<Product> productPage =  productService.getByProductCategory(pageRequest, category_id);

            Page<ProductResponse> productResponses = productPage.map(product -> ProductResponse.fromProduct(product));
            List<ProductResponse> productResponseList = productResponses.getContent();
            return ResponseEntity.ok(ListProductResponse.builder()
                            .productResponse(productResponseList)
                            .totalPages(productResponses.getTotalPages())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedProduct(
            @ModelAttribute PaginationRequest paginationRequest
    ){
        try {
            PageRequest pageRequest = PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getLimit(),
                    Sort.by("stockQuantity").ascending()
            );
            Page<Product> productPage = productService.getByProductCategory(pageRequest,paginationRequest.getCategory_id());
            Page<ProductResponse> productResponses = productPage.map(product -> ProductResponse.fromProduct(product));
            List<ProductResponse> productResponseList = productResponses.getContent();
            return ResponseEntity.ok(ListProductResponse.builder()
                    .productResponse(productResponseList)
                    .totalPages(productResponses.getTotalPages())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam("product") String productDtoJson,
            @RequestParam("file") MultipartFile file
    ){
        try{
            ProductDTO productDTO = objectMapper.readValue(productDtoJson, ProductDTO.class);
            if(file.getSize() == 0 || file.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("File is empty! Please upload a valid file.");
            }
            if(file.getSize() > 10 * 1024 * 1024){
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too large! Maximum size is 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }
            String fileName = saveFile(file);
            productDTO.setImageUrl(fileName);

            return ResponseEntity.ok(productService.createProduct(productDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping("image-product/{image-name}")
    public ResponseEntity<?> viewImage(@PathVariable("image-name") String imageName ){
        try {
            // xây dựng một đường dẫn đầy đủ đến nơi mà một tệp ảnh có tên là imageName
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{product-id}")
    public ResponseEntity<?> getProductDetail(
            @PathVariable("product-id") Long productId
    ){
        try {
            Product product = productService.getProductDetail(productId);
            ProductResponse productRepository = ProductResponse.fromProduct(product);

            return ResponseEntity.ok(productRepository);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}