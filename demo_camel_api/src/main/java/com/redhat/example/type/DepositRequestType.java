package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.redhat.example.type.ServiceRequestType;
import com.redhat.example.entity.DepositAllocationDataEntity;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class DepositRequestType extends ServiceRequestType {

    /** 個人契約番号 */
    private String customer_contract_number;

    /** 入金日 */
    private String deposit_date;

    /** 顧客請求締年月日 */
    private String customer_billing_due_date;

    /** 決済日 */
    private String contract_settlement_date;

    /** 入金種類区分 */
    private String deposit_category_code;

    /** 入金額 */
    private BigDecimal deposit_amount;

    /** 過剰金調整区分 */
    private String excess_money_handling_category;

    /** 入金充当情報 */
    private DepositAllocationDataEntity deposit_allocation_data;

    /** コンストラクタ */
    public DepositRequestType() {}

    /** コンストラクタ */
    public DepositRequestType(
        String request_id, String customer_contract_number, 
        String deposit_date, String customer_billing_due_date, String contract_settlement_date, 
        String deposit_category_code, BigDecimal deposit_amount, String excess_money_handling_category, 
        DepositAllocationDataEntity deposit_allocation_data) {
            super.setRequest_id(request_id);
            this.setCustomer_contract_number(customer_contract_number);
            this.setDeposit_date(deposit_date);
            this.setCustomer_billing_due_date(customer_billing_due_date);
            this.setContract_settlement_date(contract_settlement_date);
            this.setDeposit_category_code(deposit_category_code);
            this.setDeposit_amount(deposit_amount);
            this.setExcess_money_handling_category(excess_money_handling_category);
            this.setDeposit_allocation_data(deposit_allocation_data);
    }

}
