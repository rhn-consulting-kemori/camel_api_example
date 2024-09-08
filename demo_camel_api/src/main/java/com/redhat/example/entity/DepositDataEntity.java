package com.redhat.example.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DepositDataEntity {
    
    /** 入金種類区分 */
    private String deposit_category_code;
    
    /** 入金充当額 */
    private SaikenCompositeUnitEntity deposit_allocation_amount;

    /** 過剰金 */
    private BigDecimal excess_money;
    
    /** 残請求予定額 */
    private SeikyuCompositeUnitEntity estimated_billing_amount;
    
    /** 残高 */
    private SaikenCompositeUnitEntity balance_amount;

}
