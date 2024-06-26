package com.redhat.example.rule;

// Utilities
import java.util.Arrays;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

// Camel
import org.apache.camel.Exchange;

// Spring
import org.springframework.stereotype.Component;

// Business Object
import com.redhat.example.type.DepositEntryCheckRequestType;
import com.redhat.example.type.DepositEntryCheckResponseType;

@Component
public class DepositEntryCheckRule {

    public void check(Exchange exchange) {

        /**
         * Exchange IN
         */
        DepositEntryCheckRequestType request_body = exchange.getMessage().getBody(DepositEntryCheckRequestType.class);

        // Set Request
        DepositEntryCheckResponseType exchange_message = new DepositEntryCheckResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        // Rule
        /**
         * Null
         * - request_id
         */
        if(request_body.getRequest_id().length() == 0) {
            exchange_message.setResult("1", "E01", "request_id: null error");
        }
        
        /** 
         * Numeric & Length & Exsist
         * - card_number
         * - customer_contract_number
         */
        if(!check_length(request_body.getCard_number(), 16)) {
            exchange_message.setResult("1", "E01", "card_number: length error");
        }
        if(!check_numerical(request_body.getCard_number())){
            exchange_message.setResult("1", "E02", "card_number: numeric error");
        }

        // -------------------------------------------------------------------------
        if(!check_length(request_body.getCustomer_contract_number(), 10)) {
            exchange_message.setResult("1", "E01", "customer_contract_number: length error");
        }
        if(!check_numerical(request_body.getCustomer_contract_number())){
            exchange_message.setResult("1", "E02", "customer_contract_number: numeric error");
        }

        // -------------------------------------------------------------------------
        if(!checkCustomer(request_body.getCustomer_contract_number())) {
            exchange_message.setResult("1", "E06", "customer_contract_number: non exist error");
        }

        /**
         * Date
         * - customer_billing_due_date
         * - contract_settlement_date
         * - deposit_date
         */
        if(!check_date(request_body.getCustomer_billing_due_date())) {
            exchange_message.setResult("1", "E03", "customer_billing_due_date: date error");
        }

        if(!check_date(request_body.getContract_settlement_date())) {
            exchange_message.setResult("1", "E03", "contract_settlement_date: date error");
        }
        
        if(!check_date(request_body.getDeposit_date())) {
            exchange_message.setResult("1", "E03", "deposit_date: date error");
        }

        /**
         * Size > 0
         * - deposit_amount
         */
        if(request_body.getDeposit_amount().compareTo(new BigDecimal(0)) <= 0){
            exchange_message.setResult("1", "E04", "deposit_amount: zero error");
        }

        /**
         * Code
         * - excess_money_handling_category
         */
        String[] excess_money_handling_code = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(!Arrays.asList(excess_money_handling_code).contains(request_body.getExcess_money_handling_category())){
            exchange_message.setResult("1", "E05", "excess_money_handling_category: code error");
        }

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }

    /** 桁数チェック */
    private boolean check_length(String text, int str_length){
        if(text.length() == str_length) {
            return true;
        } else {
            return false;
        }
    }

    /** 数値チェック */
    private boolean check_numerical(String text){
        return text.matches("[+-]?\\d*(\\.\\d+)?");
    }

    /** 日付チェック */
    private boolean check_date(String strDate) {
        try {
            strDate = strDate.substring(0,4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6,8);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            Date result = df.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 顧客存在チェック */
    public boolean checkCustomer(String customer_number) {
        // 便宜的に、最初の1桁が０の会員のみ存在するものとする。
        if(customer_number.substring(0,1).equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}