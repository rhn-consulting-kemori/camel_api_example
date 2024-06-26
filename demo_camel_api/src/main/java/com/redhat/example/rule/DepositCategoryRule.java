package com.redhat.example.rule;

// Utilities
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

// Camel
import org.apache.camel.Exchange;

// Spring
import org.springframework.stereotype.Component;

// Business Object
import com.redhat.example.type.DepositCategoryRequestType;
import com.redhat.example.type.DepositCategoryResponseType;

@Component
public class DepositCategoryRule {

    public void categorise(Exchange exchange) {

        /**
         * Exchange IN
         */
        DepositCategoryRequestType request_body = exchange.getMessage().getBody(DepositCategoryRequestType.class);

        // Set Request
        DepositCategoryResponseType exchange_message = new DepositCategoryResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        // Date Convert
        Date customer_billing_due_date = date_convert(request_body.getCustomer_billing_due_date());
        Date contract_settlement_date = date_convert(request_body.getContract_settlement_date());
        Date deposit_date = date_convert(request_body.getDeposit_date());

        // 締処理日；請求締の20日
        Date shime_process_date = getCalcurateDate(customer_billing_due_date, 5);

        // 前訂正締；5営業日前
        Date five_day_before = getCalcurateDate(contract_settlement_date, -5);

        // 期日入金締；約定決済日＋１日
        Date kijitu_deposit_due = getCalcurateDate(contract_settlement_date, 1);

        // 入金種類判定
        if(deposit_date.before(shime_process_date) || deposit_date.after(kijitu_deposit_due)) {
            // Error
            exchange_message.setResult("1", "E01", "out of range error at kijitsu_deposit");
        } else {
            // 前訂正入金
            if(deposit_date.compareTo(five_day_before) < 1) {
                exchange_message.setDeposit_category_code("zenteisei");
            // 事前入金
            } else if(deposit_date.compareTo(contract_settlement_date) < 0) {
                exchange_message.setDeposit_category_code("jizen");
            // 期日入金
            } else if(deposit_date.compareTo(kijitu_deposit_due) < 1) {
                exchange_message.setDeposit_category_code("kijitsu");
            } else {
                // Error
                exchange_message.setResult("1", "E01", "out of range error at kijitsu_deposit");
            }
        }

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }

    /** 日付変換 */
    private Date date_convert(String strDate) {
        try {
            strDate = strDate.substring(0,4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6,8);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            Date result = df.parse(strDate);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    // 日付計算
    private Date getCalcurateDate(Date base_date, int date_length) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(base_date);
        calendar.add(Calendar.DAY_OF_MONTH, date_length);
        return calendar.getTime();
    }

}
