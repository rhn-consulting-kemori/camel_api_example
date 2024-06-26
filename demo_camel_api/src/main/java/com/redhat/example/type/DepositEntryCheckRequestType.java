package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class DepositEntryCheckRequestType extends ServiceRequestType {

    /** カード番号 */
    private String card_number;

    /** 顧客契約番号 */
    private String customer_contract_number;

    /** 顧客請求締年月日 */
    private String customer_billing_due_date;

    /** 約定決済年月日 */
    private String contract_settlement_date;

    /** 入金年月日 */
    private String deposit_date;

    /** 入金額 */
    private BigDecimal deposit_amount;

    /** 過剰金取扱区分 */
    private String excess_money_handling_category;

    // コンストラクタ
    public DepositEntryCheckRequestType(){}
    public DepositEntryCheckRequestType(String request_id, String card_number, String customer_contract_number, String customer_billing_due_date, String contract_settlement_date, String deposit_date, BigDecimal deposit_amount, String excess_money_handling_category){
        super.setRequest_id(request_id);
        this.setCard_number(card_number);
        this.setCustomer_contract_number(customer_contract_number);
        this.setCustomer_billing_due_date(customer_billing_due_date);
        this.setContract_settlement_date(contract_settlement_date);
        this.setDeposit_date(deposit_date);
        this.setDeposit_amount(deposit_amount);
        this.setExcess_money_handling_category(excess_money_handling_category);
    }
}
