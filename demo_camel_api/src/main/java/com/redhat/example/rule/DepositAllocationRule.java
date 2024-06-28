package com.redhat.example.rule;

import java.math.BigDecimal;

// Camel
import org.apache.camel.Exchange;

// Spring
import org.springframework.stereotype.Component;

// Business Object
import com.redhat.example.type.DepositAllocationRequestType;
import com.redhat.example.type.DepositAllocationResponseType;

import main.java.com.redhat.example.entity.DepositAllocationDataEntity;

import com.redhat.example.entity.AvailableDepositAmountDataEntity;
import com.redhat.example.entity.SaikenCompositeUnitEntity;
import com.redhat.example.entity.SeikyuCompositeUnitEntity;

@Component
public class DepositAllocationRule {
    public void simulate(Exchange exchange) {

        /**
         * Exchange IN
         */
        DepositAllocationRequestType request_body = exchange.getMessage().getBody(DepositAllocationRequestType.class);

        // Set Request
        DepositAllocationResponseType exchange_message = new DepositAllocationResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        // 情報取得
        SeikyuCompositeUnitEntity seikyu_amount = request_body.getDeposit_available_amount_data().getEstimated_billing_amount();
        SaikenCompositeUnitEntity deposit_available = request_body.getDeposit_available_amount_data().getDeposit_available_amount();
        BigDecimal deposit_amount = request_body.getDeposit_amount();

        BigDecimal principal_amount = deposit_available.getTotal_amout().getPrincipal_amount();
        BigDecimal interest_amount = deposit_available.getTotal_amout().getInterest_amount();
        BigDecimal sum_amount = principal_amount.add(interest_amount);

        if(deposit_amount.compareTo(sum_amount) > 0) {
            // 過剰金が発生
        } else if(deposit_amount.compareTo(sum_amount) == 0) {
            // ピッタリ全額入金
        } else {
            // 一部入金
        }

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }

    // 過剰金が発生
    public DepositAllocationDataEntity excessAllocation(
        SeikyuCompositeUnitEntity seikyu_amount, SaikenCompositeUnitEntity deposit_available, 
        BigDecimal deposit_amount, BigDecimal principal_amount, BigDecimal interest_amount, BigDecimal sum_amount) {
        
        DepositAllocationDataEntity allocation_data = new DepositAllocationDataEntity();
        allocation_data.setDeposit_allocation_amount(deposit_available);
        seikyu_amount.getTotal_billing().setBilling_principal_amount(BigDecimal.ZERO);
        seikyu_amount.getTotal_billing().setBilling_interest_amount(BigDecimal.ZERO);
        seikyu_amount.getTotal_billing().setDeposit_principal_amount();
        seikyu_amount.getTotal_billing().setDeposit_interest_amount();


    }

    // ピッタリ全額入金
    public DepositAllocationDataEntity wholeAllocation() {

    }

    // 一部入金
    public DepositAllocationDataEntity partialAllocation() {

    }

}
