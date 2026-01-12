package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 문제: 메서드 파라미터명 불일치
     * 원인: findAllByCategory 메서드의 파라미터명이 name인데 실제로는 category를 받음. 쿼리 탐색이 cateory로 처리됨
     * 개선안:
     * - 파라미터명을 category로 변경: Page<Product> findAllByCategory(String category, Pageable pageable)
     *
     * 문제: 페이징에 order by절 부재
     * 원인: 페이징에 order by가 없는경우 페이지 별 데이터 보장이 어려움
     * 개선안: 명시적 orderby 절 추가
     */
    Page<Product> findAllByCategory(String name, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
