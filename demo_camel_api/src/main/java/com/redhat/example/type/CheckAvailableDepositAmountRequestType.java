package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import com.redhat.example.type.ServiceRequestType;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class CheckAvailableDepositAmountRequestType extends ServiceRequestType {
    
    /** 個人契約番号 */
    private String customer_contract_number;

    /** 入金日 */
    private String deposit_date;

    /** 顧客請求締年月日 */
    private String customer_billing_due_date;

    /** 約定決済日 */
    private String contract_settlement_date;

    /** 入金種類区分 */
    private String deposit_category_code;

    /** コンストラクタ */
    public CheckAvailableDepositAmountRequestType() {}

    /** コンストラクタ */
    public CheckAvailableDepositAmountRequestType(
        String request_id, String customer_contract_number, String deposit_date, String customer_billing_due_date, String contract_settlement_date, String deposit_category_code) {
        super.setRequest_id(request_id);
        this.setCustomer_contract_number(customer_contract_number);
        this.setDeposit_date(deposit_date);
        this.setCustomer_billing_due_date(customer_billing_due_date);
        this.setContract_settlement_date(contract_settlement_date);
        this.setDeposit_category_code(deposit_category_code);
    }

}
