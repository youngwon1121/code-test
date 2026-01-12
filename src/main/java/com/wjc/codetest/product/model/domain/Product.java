package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
/**
     * 문제: Setter사용
     * 원인: Setter의 무분별한 사용으로 인해 Entity가 어디서 수정되는지 추적이 어려워짐. 
     * 개선안: Product updateProduct(UpdateProductRequest request) 같은 명시적인 함수로 개선
*/
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * 문제 : GenerationType.Auto 사용
     * 원인 : GenerationType.Auto 사용시 데이터베이스에 따라 자동으로 생성되는 값이 다르게 저장될 수 있음.
     * 개선안 : GenerationType.IDENTITY와 같은 명시적 전략 사용
     */
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    /**
    * 문제 : getter 중복생성
    * 개선안 : lombok이 있으므로 제거
    * */
    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
