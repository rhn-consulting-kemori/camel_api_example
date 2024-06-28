package com.redhat.example.entity;

import lombok.Data;

@Data
public class AvailableDepositAmountDataEntity {

    /** 請求予定額 */
    private SeikyuCompositeUnitEntity estimated_billing_amount;

    /** 入金可能額 */
    private SaikenCompositeUnitEntity deposit_available_amount;
    
}
