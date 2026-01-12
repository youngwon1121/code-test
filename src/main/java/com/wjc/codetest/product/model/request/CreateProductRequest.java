package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 문제: 기본 생성자 부재
 * 원인:
 * - 기본생성자 부재로 오류 발생
 * 개선안:
 * - 기본생성자 추가 or Java 17의 record 기능 활용
 * 
 * 문제: 보안 - 입력값 검증 어노테이션 부재
 * 원인: 필드에 @NotNull, @NotBlank 등의 검증 어노테이션이 없음
 * 개선안:
 * - jakarta.validation.constraints 패키지의 @NotBlank, @Size(max=100) 어노테이션 추가 */
@Getter
@Setter
public class CreateProductRequest {
    private String category;
    private String name;

    public CreateProductRequest(String category) {
        this.category = category;
    }

    public CreateProductRequest(String category, String name) {
        this.category = category;
        this.name = name;
    }
}

