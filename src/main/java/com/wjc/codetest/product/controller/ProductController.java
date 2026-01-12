package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 문제: RESTful API 설계 - URL 경로 패턴 위반 및 일관성 부재
 * 원인: 
 * - URL에 동사 포함 (/get, /create, /delete, /update) - RESTful 원칙 위반
 * - 리소스명이 단수/복수 혼용 (product vs products)
 * - 일관성 없는 경로 패턴 (/get/product/by/, /create/product, /product/list 등)
 * 개선안:
 * - 리소스는 명사로 표현하고 HTTP 메서드로 액션 표현: GET /products/{id}, POST /products, DELETE /products/{id}, PUT/PATCH /products/{id}
 * - 리소스명은 복수형 사용 (products)
 * - @RequestMapping("/products")를 클래스 레벨에 추가하여 기본 경로 통일
 * - 선택 근거: RESTful API 표준 컨벤션 준수, 가독성 및 일관성 향상
 * 
 * 문제: RESTful API 설계 - 적절한 HTTP 상태 코드 미사용 (모든 응답이 200 OK)
 * 원인: 모든 메서드에서 ResponseEntity.ok()로 200 OK만 반환
 * 개선안:
 * - 생성 성공: 201 Created (createProduct)
 * - 삭제 성공: 204 No Content (deleteProduct)
 * - 조회 성공: 200 OK (getProductById, getProductListByCategory 등)
 * - 리소스 없음: 404 Not Found (getProductById에서 존재하지 않는 경우)
 * - 선택 근거: HTTP 상태 코드의 의미론적 정확성, 클라이언트가 응답 상태를 명확히 파악 가능
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // Path 개선: GET /products/{productId}
    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        // 상태 코드: 존재하지 않으면 404, 존재하면 200 OK
        return ResponseEntity.ok(product);
    }

    // Path 개선: POST /products
    // 상태 코드 개선: 201 Created 반환 (ResponseEntity.status(HttpStatus.CREATED).body(product))
    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    // Path 개선: DELETE /products/{productId} (HTTP 메서드도 DELETE로 변경)
    // 상태 코드 개선: 204 No Content 반환 (ResponseEntity.noContent().build())
    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    // Path 개선: PUT/PATCH /products/{productId} (HTTP 메서드도 PUT 또는 PATCH로 변경, ID는 path variable로)
    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    // Path 개선: GET /products?category=xxx&page=0&size=10 (HTTP 메서드를 GET으로 변경, 쿼리 파라미터 사용)
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    // Path 개선: GET /products/categories
    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}