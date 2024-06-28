package com.redhat.example.rule;

// Camel
import org.apache.camel.Exchange;

// Spring
import org.springframework.stereotype.Component;

// Business Object
import com.redhat.example.type.CheckAvailableDepositAmountRequestType;
import com.redhat.example.type.CheckAvailableDepositAmountResponseType;

@Component
public class DepositRule {
    public void deposit(Exchange exchange) {

        /**
         * Exchange IN
         */
        CheckAvailableDepositAmountRequestType request_body = exchange.getMessage().getBody(CheckAvailableDepositAmountRequestType.class);

        // Set Request
        CheckAvailableDepositAmountResponseType exchange_message = new CheckAvailableDepositAmountResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }
}
