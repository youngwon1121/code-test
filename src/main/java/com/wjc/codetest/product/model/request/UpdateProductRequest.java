package com.wjc.codetest.product.model.request;

import lombok.Getter;
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
 * - jakarta.validation.constraints 패키지의 @NotBlank 등의 어노테이션 추가 */
@Getter
@Setter
public class UpdateProductRequest {
    private Long id;
    private String category;
    private String name;

    public UpdateProductRequest(Long id) {
        this.id = id;
    }

    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}

