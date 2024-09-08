package com.redhat.example.processor;

// Camel
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

// Spring
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

// Rule
import com.redhat.example.rule.DepositCategoryRule;
import com.redhat.example.config.AppConfig;
import com.redhat.example.rule.CheckAvailableDepositAmountRule;
import com.redhat.example.rule.DepositAllocationRule;
import com.redhat.example.rule.DepositRule;

// Business Object
import com.redhat.example.type.KijitsuAllocationDepositRequestType;
import com.redhat.example.type.KijitsuAllocationDepositResponseType;
import com.redhat.example.type.DepositCategoryRequestType;
import com.redhat.example.type.DepositCategoryResponseType;
import com.redhat.example.type.CheckAvailableDepositAmountRequestType;
import com.redhat.example.type.CheckAvailableDepositAmountResponseType;
import com.redhat.example.type.DepositAllocationRequestType;
import com.redhat.example.type.DepositAllocationResponseType;
import com.redhat.example.type.DepositRequestType;
import com.redhat.example.type.DepositResponseType;

@Component
public class KijitsuAllocationDepositProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        /**
         * Exchange IN
         */
        KijitsuAllocationDepositRequestType request_body = exchange.getMessage().getBody(KijitsuAllocationDepositRequestType.class);

        // Set Request
        KijitsuAllocationDepositResponseType exchange_message = new KijitsuAllocationDepositResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        // 入金種類区分
        DepositCategoryRequestType category_request = new DepositCategoryRequestType(
            request_body.getRequest_id(), 
            request_body.getCustomer_contract_number(), 
            request_body.getCustomer_billing_due_date(), 
            request_body.getContract_settlement_date(), 
            request_body.getDeposit_date()
        );
        exchange.getMessage().setBody(category_request);
        
        DepositCategoryRule deposit_category_rule = new DepositCategoryRule();
        deposit_category_rule.categorise(exchange);
        DepositCategoryResponseType category_response = exchange.getMessage().getBody(DepositCategoryResponseType.class);

        exchange_message.setResult(
            category_response.getResponse_result(), 
            category_response.getErr_code(), 
            category_response.getErr_context()
        );

        // 入金可能額照会
        CheckAvailableDepositAmountRequestType check_available_deposit_amount_request = new CheckAvailableDepositAmountRequestType(
            request_body.getRequest_id(), 
            request_body.getCustomer_contract_number(), 
            request_body.getDeposit_date(), 
            request_body.getCustomer_billing_due_date(), 
            request_body.getContract_settlement_date(), 
            category_response.getDeposit_category_code()
        );
        exchange.getMessage().setBody(check_available_deposit_amount_request);

        CheckAvailableDepositAmountRule check_available_deposit_amount_rule = new CheckAvailableDepositAmountRule();
        check_available_deposit_amount_rule.simulate(exchange);
        CheckAvailableDepositAmountResponseType check_available_deposit_amount_response = exchange.getMessage().getBody(CheckAvailableDepositAmountResponseType.class);

        exchange_message.setResult(
            check_available_deposit_amount_response.getResponse_result(), 
            check_available_deposit_amount_response.getErr_code(), 
            check_available_deposit_amount_response.getErr_context()
        );

        // 入金充当額試算
        DepositAllocationRequestType deposit_allocation_request = new DepositAllocationRequestType(
            request_body.getRequest_id(), 
            request_body.getCustomer_contract_number(), 
            request_body.getDeposit_date(), 
            request_body.getCustomer_billing_due_date(), 
            request_body.getContract_settlement_date(), 
            category_response.getDeposit_category_code(),
            request_body.getDeposit_amount(), 
            request_body.getExcess_money_handling_category(), 
            check_available_deposit_amount_response.getDeposit_available_amount_data()
        );
        exchange.getMessage().setBody(deposit_allocation_request);

        DepositAllocationRule deposit_allocation_rule = new DepositAllocationRule();
        deposit_allocation_rule.simulate(exchange);
        DepositAllocationResponseType deposit_allocation_response = exchange.getMessage().getBody(DepositAllocationResponseType.class);

        exchange_message.setResult(
            deposit_allocation_response.getResponse_result(), 
            deposit_allocation_response.getErr_code(), 
            deposit_allocation_response.getErr_context()
        );

        // 入金
        DepositRequestType deposit_request = new DepositRequestType(
            request_body.getRequest_id(), 
            request_body.getCustomer_contract_number(), 
            request_body.getDeposit_date(), 
            request_body.getCustomer_billing_due_date(), 
            request_body.getContract_settlement_date(), 
            category_response.getDeposit_category_code(),
            request_body.getDeposit_amount(), 
            request_body.getExcess_money_handling_category(), 
            deposit_allocation_response.getDeposit_allocation_data()
        );
        exchange.getMessage().setBody(deposit_request);

        DepositRule deposit_rule = new DepositRule();
        deposit_rule.deposit(exchange);
        DepositResponseType deposit_response = exchange.getMessage().getBody(DepositResponseType.class);

        exchange_message.setDeposit_data(deposit_response.getDeposit_data());
        exchange_message.setResult(
            deposit_response.getResponse_result(), 
            deposit_response.getErr_code(), 
            deposit_response.getErr_context()
        );

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);
    }

}
