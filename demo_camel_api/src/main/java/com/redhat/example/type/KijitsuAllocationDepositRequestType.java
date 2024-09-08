package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import com.redhat.example.type.ServiceRequestType;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class KijitsuAllocationDepositRequestType extends ServiceRequestType {

    /** 顧客契約番号 */
    private String customer_contract_number;

    /** 入金日 */
    private String deposit_date;

    /** 顧客請求締年月日 */
    private String customer_billing_due_date;

    /** 決済日 */
    private String contract_settlement_date;

    /** 入金額 */
    private BigDecimal deposit_amount;

    /** 過剰金調整区分 */
    private String excess_money_handling_category;

    /** コンストラクタ */
    public KijitsuAllocationDepositRequestType() {}

    /** コンストラクタ */
    public KijitsuAllocationDepositRequestType(
        String request_id, String customer_contract_number, String deposit_date, String customer_billing_due_date, String contract_settlement_date, 
        BigDecimal deposit_amount, String excess_money_handling_category) {
            super.setRequest_id(request_id);
            this.setCustomer_contract_number(customer_contract_number);
            this.setDeposit_date(deposit_date);
            this.setCustomer_billing_due_date(customer_billing_due_date);
            this.setContract_settlement_date(contract_settlement_date);
            this.setDeposit_amount(deposit_amount);
            this.setExcess_money_handling_category(excess_money_handling_category);
    }
}
