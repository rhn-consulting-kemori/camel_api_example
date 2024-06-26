package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class DepositCategoryRequestType extends ServiceRequestType {

    /** 顧客契約番号 */
    private String customer_contract_number;

    /** 顧客請求締年月日 */
    private String customer_billing_due_date;

    /** 約定決済年月日 */
    private String contract_settlement_date;

    /** 入金年月日 */
    private String deposit_date;

    /** コンストラクタ */
    public DepositCategoryRequestType() {}

    /** コンストラクタ */
    public DepositCategoryRequestType(String request_id, String customer_contract_number, String customer_billing_due_date, String contract_settlement_date, String deposit_date) {
        super.setRequest_id(request_id);
        this.setCustomer_contract_number(customer_contract_number);
        this.setCustomer_billing_due_date(customer_billing_due_date);
        this.setContract_settlement_date(contract_settlement_date);
        this.setDeposit_date(deposit_date);
    }

}