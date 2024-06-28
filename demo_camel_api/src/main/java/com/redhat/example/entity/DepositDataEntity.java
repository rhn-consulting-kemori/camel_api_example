package com.redhat.example.entity;

import lombok.Data;

@Data
public class DepositDataEntity {
    
    /** 入金充当額 */
    private SaikenCompositeUnitEntity deposit_allocation_amount;

    /** 残請求予定額 */
    private SeikyuCompositeUnitEntity estimated_billing_amount;
    
    /** 残高 */
    private SaikenCompositeUnitEntity balance_amount;

}
