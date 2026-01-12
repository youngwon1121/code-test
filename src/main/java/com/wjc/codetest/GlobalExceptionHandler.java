package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 문제: 보안 - 예외 응답 body가 비어있어 클라이언트에 에러 정보가 전달되지 않음
 * 원인: ResponseEntity.status().build()로 빈 응답을 반환하고 있음
 * 개선안:
 * - ApiExceptionCode enum 정의: 에러 코드, 메시지, HTTP 상태 코드를 포함한 enum 생성
 *   - 예: enum ApiExceptionCode { NOT_FOUND("4041", "Resource not found.", HttpStatus.NOT_FOUND) }
 * - CustomException 클래스 생성: ApiExceptionCode를 받아서 httpStatus, errorCode, errorMessage를 포함하는 예외 클래스
 * - @ExceptionHandler(CustomException.class) 메서드에서 ErrorResponse.of()로 응답 생성 후 반환
 * - 선택 근거: 에러 정보의 중앙 관리, 일관된 API 응답 구조, 적절한 HTTP 상태 코드 반환
 * 
 * 문제: 에러 처리 - 예외 처리 범위가 너무 넓음 (RuntimeException 전체 처리)
 * 원인: @ExceptionHandler(RuntimeException.class)로 모든 RuntimeException을 500으로 처리
 * 개선안:
 * - CustomException을 상속받은 구체적인 예외 타입별로 핸들러 분리
 * - 각 예외 타입에 맞는 ApiExceptionCode enum 값 사용
 */
@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
