package com.redhat.example.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaikenSimpleUnitEntity {

    /** 元本額 */
    private BigDecimal principal_amount;

    /** 利息額 */
    private BigDecimal interest_amount;

    /** コンストラクタ */ 
    public SaikenSimpleUnitEntity() {
        this.principal_amount = BigDecimal.ZERO;
        this.interest_amount = BigDecimal.ZERO;
    }
    
     /** コンストラクタ */
    public SaikenSimpleUnitEntity(BigDecimal principal_amount, BigDecimal interest_amount) {
        this.setPrincipal_amount(principal_amount);
        this.setInterest_amount(interest_amount);
    }
    
}
