package com.redhat.example.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SeikyuSimpleUnitEntity {

    /** 請求元本額 */
    private BigDecimal billing_principal_amount;

    /** 請求利息額 */
    private BigDecimal billing_interest_amount;

    /** 入金元本額 */
    private BigDecimal deposit_principal_amount;

    /** 入金利息額 */
    private BigDecimal deposit_interest_amount;

    /** コンストラクタ */
    public SeikyuSimpleUnitEntity() {
        this.setBilling_principal_amount(BigDecimal.ZERO);
        this.setBilling_interest_amount(BigDecimal.ZERO);
        this.setDeposit_principal_amount(BigDecimal.ZERO);
        this.setDeposit_interest_amount(BigDecimal.ZERO);
    }

    /** コンストラクタ 各項目設定 */
    public SeikyuSimpleUnitEntity(BigDecimal billing_principal_amount, BigDecimal billing_interest_amount, BigDecimal deposit_principal_amount, BigDecimal deposit_interest_amount) {
        this.setBilling_principal_amount(billing_principal_amount);
        this.setBilling_interest_amount(billing_interest_amount);
        this.setDeposit_principal_amount(deposit_principal_amount);
        this.setDeposit_interest_amount(deposit_interest_amount);
    }

}
