package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 문제: 설계 - 트랜잭션 관리 어노테이션 부재
 * 원인: 서비스 클래스에 @Transactional 어노테이션이 없음
 *       - 데이터 변경 작업(create, update, delete)이 트랜잭션 없이 실행됨
 *       - 예외 발생 시 롤백이 보장되지 않음
 * 개선안:
 * - 클래스 레벨에 @Transactional 추가: 기본적으로 모든 메서드에 트랜잭션 적용
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    /**
     * 문제: 에러 처리 - 존재하지 않는 리소스에 대해 부적절한 예외 타입과 HTTP 상태 코드 사용
     * 원인: RuntimeException을 던져서 GlobalExceptionHandler에서 500으로 처리됨 
     * 개선안:
     * - throw new CustomException(ApiExceptionCode.NOT_FOUND) 형태로 예외 발생
     * - ApiExceptionCode enum에 NOT_FOUND("4041", "Resource not found.", HttpStatus.NOT_FOUND) 정의
     * - GlobalExceptionHandler에서 CustomException을 잡아 ErrorResponse 반환
     * - 선택 근거: 적절한 HTTP 상태 코드(404) 반환, 에러 정보의 중앙 관리
     * 
     * 문제: 코드 품질 - Optional 처리 방식이 비효율적
     * 원인: !productOptional.isPresent()와 productOptional.get()을 분리하여 사용
     *       - Optional의 함수형 프로그래밍 스타일을 활용하지 않음
     * 개선안:
     * - productOptional.orElseThrow(() -> new CustomException(ApiExceptionCode.NOT_FOUND)) 사용
     * - 또는 productOptional.ifPresentOrElse() 활용
     * - 선택 근거: 코드 간결성 향상, Optional의 의도에 맞는 사용, 가독성 개선
     */
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;

    }

    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}