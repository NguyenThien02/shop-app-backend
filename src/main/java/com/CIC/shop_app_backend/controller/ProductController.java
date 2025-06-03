package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.PaginationRequest;
import com.CIC.shop_app_backend.dtos.ProductDTO;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.responses.ListProductResponse;
import com.CIC.shop_app_backend.responses.MessageResponse;
import com.CIC.shop_app_backend.responses.ProductResponse;
import com.CIC.shop_app_backend.services.IProductRedisService;
import com.CIC.shop_app_backend.services.IProductService;
import com.CIC.shop_app_backend.services.Impl.ProductRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final ObjectMapper objectMapper;
    private final IProductRedisService productRedisService;

    @GetMapping("/by-category")
    public ResponseEntity<?> getProductByCategory(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    limit,
                    Sort.by("productId").descending()
            );
            ListProductResponse listProductResponse = productRedisService
                    .getProductByCategory(categoryId, pageRequest);

            if(listProductResponse == null){
                Page<Product> productPage = productService.getByProductCategory(pageRequest, categoryId);
                Page<ProductResponse> productPageResponse = productPage.map(product -> ProductResponse.fromProduct(product));
                List<ProductResponse> productResponses = productPageResponse.getContent();
                int totalPages = productPageResponse.getTotalPages();
                listProductResponse = ListProductResponse.builder()
                        .productResponse(productResponses)
                        .totalPages(productPageResponse.getTotalPages())
                        .build();
                productRedisService.saveAllProducts(listProductResponse,categoryId,pageRequest);
            }


            return ResponseEntity.ok(listProductResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedProduct(
            @ModelAttribute PaginationRequest paginationRequest,
            @RequestParam(defaultValue = "0", name = "category_id") Long category_id
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getLimit(),
                    Sort.by("stockQuantity").ascending()
            );
            Page<Product> productPage = productService.getByProductCategory(pageRequest, category_id);
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
    ) {
        try {
            ProductDTO productDTO = objectMapper.readValue(productDtoJson, ProductDTO.class);
            if (file.getSize() == 0 || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("File is empty! Please upload a valid file.");
            }
            if (file.getSize() > 10 * 1024 * 1024) {
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
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping("image-product/{image-name}")
    public ResponseEntity<?> viewImage(@PathVariable("image-name") String imageName) {
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
    ) {
        try {
            Product product = productService.getProductDetail(productId);
            ProductResponse productRepository = ProductResponse.fromProduct(product);

            return ResponseEntity.ok(productRepository);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/upload-file-products")
    public ResponseEntity<?> uploadFileProducts(@RequestParam("fileProduct") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.badRequest().body("Invalid file.");
        }

        try {
            if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                processExcel(file.getInputStream());
                return ResponseEntity.ok(new MessageResponse("File uploaded and processed successfully.",true));
            } else {
                return ResponseEntity.badRequest().body("Only CSV or Excel files are supported.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to process file: " + e.getMessage());
        }
    }

    private void processExcel(InputStream inputStream) throws IOException {
        List<ProductDTO> listProductDTO = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(inputStream);

        Sheet sheet = workbook.getSheetAt(0);

        boolean isFirstRow = true;

        for (Row row : sheet) {
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }

            if (isRowEmpty(row)) {
                continue;
            }

            ProductDTO productDTO = new ProductDTO();

            productDTO.setProductName(getCellString(row.getCell(0)));
            productDTO.setDescription(getCellString(row.getCell(1)));
            productDTO.setPrice(getCellDouble(row.getCell(2)));
            productDTO.setStockQuantity(getCellLong(row.getCell(3)));
            productDTO.setImageUrl(getCellString(row.getCell(4)));
            productDTO.setCategoryId(getCellLong(row.getCell(5)));
            productDTO.setSellerId(getCellLong(row.getCell(6)));

            listProductDTO.add(productDTO);
        }

        for (ProductDTO productDTO : listProductDTO){
            productService.createProduct(productDTO);
        }
        workbook.close();
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;

        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }


    private String getCellString(Cell cell) {
        return cell != null ? cell.toString().trim() : "";
    }

    private double getCellDouble(Cell cell) {
        return cell != null ? cell.getNumericCellValue() : 0.0;
    }

    private long getCellLong(Cell cell) {
        return cell != null ? (long) cell.getNumericCellValue() : 0;
    }

}