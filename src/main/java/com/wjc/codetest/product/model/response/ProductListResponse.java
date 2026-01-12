package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductListResponse {
    /**
     * 문제: Product Entity 그대로 리턴
     * 원인: Entity를 그대로 리턴시 중요정보등을 포함하고 있을시 보안 문제 발생 가능
     * 개선안:
     * - ProductResponse 추가 후 해당 객체를 response
     */

    private List<Product> products;
    private int totalPages;
    private long totalElements;
    private int page;

    /**
     * 문제: parameter을 content로 사용
     * 원인: products를 받는 parameter명이 content로 해당 클래스 생성자 호출시 어떤 정보를 넣어줘야하는지 알기 어려움
     * 개선안:
     * - content -> products로 파라미터명 변경
     */
    public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) {
        this.products = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
