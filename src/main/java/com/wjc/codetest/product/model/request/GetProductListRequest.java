package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 문제: 코드 품질 - Java 17의 record 기능 미활용
 * 원인: 일반 클래스와 Lombok을 사용하여 DTO 구현
 * 개선안:
 * - record로 변경하여 불변 객체 보장 및 코드 간결성 향상
 * 
 * 문제: 보안 - 입력값 검증 어노테이션 부재로 음수 page, 0 이하 size 등 잘못된 입력 가능
 * 원인: 필드에 @Min, @Max 등의 검증 어노테이션이 없음
 * 개선안:
 * - @Min(0) private int page; (페이지는 0 이상)
 * - @Min(1) @Max(100) private int size; (사이즈는 1~100 사이로 제한)
 */
@Getter
@Setter
public class GetProductListRequest {
    private String category;
    private int page;
    private int size;
}