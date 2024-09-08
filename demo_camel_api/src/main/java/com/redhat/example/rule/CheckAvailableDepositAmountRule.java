package com.redhat.example.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

// Camel
import org.apache.camel.Exchange;

// Spring
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

// Configuration
import com.redhat.example.config.AppConfig;

// Business Object
import com.redhat.example.type.CheckAvailableDepositAmountRequestType;
import com.redhat.example.type.CheckAvailableDepositAmountResponseType;
import com.redhat.example.entity.AvailableDepositAmountDataEntity;
import com.redhat.example.entity.SaikenCompositeUnitEntity;
import com.redhat.example.entity.SaikenSimpleUnitEntity;
import com.redhat.example.entity.SeikyuCompositeUnitEntity;
import com.redhat.example.entity.SeikyuSimpleUnitEntity;

@Component
public class CheckAvailableDepositAmountRule {

    private AppConfig appConfig;

    public void simulate(Exchange exchange) {

        /**
         * Exchange IN
         */
        CheckAvailableDepositAmountRequestType request_body = exchange.getMessage().getBody(CheckAvailableDepositAmountRequestType.class);

        // Set Request
        CheckAvailableDepositAmountResponseType exchange_message = new CheckAvailableDepositAmountResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        // 請求予定額の設定
        Map<String, SeikyuSimpleUnitEntity> products_seikyu_map = new HashMap();
        appConfig = new AppConfig();
        products_seikyu_map.put("sp1", appConfig.getSp1_seikyu());
        products_seikyu_map.put("sprv", appConfig.getSprv_seikyu());

        SeikyuCompositeUnitEntity estimated_billing_amount = new SeikyuCompositeUnitEntity();
        estimated_billing_amount.setTotal_billing(summaryProduct(products_seikyu_map));
        estimated_billing_amount.setProducts_billing_map(products_seikyu_map);

        // 入金可能額算出
        BigDecimal sprvInterest = recalcurateInterest(
            request_body.getDeposit_date(), 
            request_body.getContract_settlement_date(), 
            products_seikyu_map.get("sprv").getBilling_principal_amount(), 
            products_seikyu_map.get("sprv").getBilling_interest_amount()
        );

        Map<String, SaikenSimpleUnitEntity> products_deposit_map = new HashMap();
        products_deposit_map.put("sp1", new SaikenSimpleUnitEntity(products_seikyu_map.get("sp1").getBilling_principal_amount(), products_seikyu_map.get("sp1").getBilling_interest_amount()));
        products_deposit_map.put("sprv", new SaikenSimpleUnitEntity(products_seikyu_map.get("sprv").getBilling_principal_amount(), sprvInterest));

        SaikenCompositeUnitEntity deposit_available_amount = new SaikenCompositeUnitEntity();
        deposit_available_amount.setTotal_amout(summaryDepositProduct(products_deposit_map));
        deposit_available_amount.setProducts_amount_map(products_deposit_map);

        AvailableDepositAmountDataEntity deposit_available_amount_data = new AvailableDepositAmountDataEntity();
        deposit_available_amount_data.setEstimated_billing_amount(estimated_billing_amount);
        deposit_available_amount_data.setDeposit_available_amount(deposit_available_amount);

        exchange_message.setDeposit_available_amount_data(deposit_available_amount_data);

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }

    /** 合計請求額算出 */
    public SeikyuSimpleUnitEntity summaryProduct(Map<String, SeikyuSimpleUnitEntity> products_map) {
        SeikyuSimpleUnitEntity total_seikyu = new SeikyuSimpleUnitEntity();
        for(Map.Entry<String, SeikyuSimpleUnitEntity> entry: products_map.entrySet()) {
            total_seikyu.setBilling_principal_amount(total_seikyu.getBilling_principal_amount().add(entry.getValue().getBilling_principal_amount()));
            total_seikyu.setBilling_interest_amount(total_seikyu.getBilling_interest_amount().add(entry.getValue().getBilling_interest_amount()));
            total_seikyu.setDeposit_principal_amount(total_seikyu.getDeposit_principal_amount().add(entry.getValue().getDeposit_principal_amount()));
            total_seikyu.setDeposit_interest_amount(total_seikyu.getDeposit_interest_amount().add(entry.getValue().getDeposit_interest_amount()));
        }
        return total_seikyu;
    }

    /** 入金合計額の算出 */
    public SaikenSimpleUnitEntity summaryDepositProduct(Map<String, SaikenSimpleUnitEntity> products_map) {
        SaikenSimpleUnitEntity total_deposit = new SaikenSimpleUnitEntity();
        for(Map.Entry<String, SaikenSimpleUnitEntity> entry: products_map.entrySet()) {
            total_deposit.setPrincipal_amount(total_deposit.getPrincipal_amount().add(entry.getValue().getPrincipal_amount()));
            total_deposit.setInterest_amount(total_deposit.getInterest_amount().add(entry.getValue().getInterest_amount()));
        }
        return total_deposit;
    }

    /** 入金利息額再計算 */
    public BigDecimal recalcurateInterest(String deposit_date, String contract_settlement_date, BigDecimal zandaka, BigDecimal interest) {
        long between_days = date_between(deposit_date, contract_settlement_date);
        if(between_days < 1) {
            return interest;
        } else {
            BigDecimal day_range = BigDecimal.valueOf(30).subtract(BigDecimal.valueOf(date_between(deposit_date, contract_settlement_date)));
            return zandaka.multiply(day_range).multiply(BigDecimal.valueOf(0.15)).divide(BigDecimal.valueOf(365), 0, RoundingMode.DOWN);
        }
    }

    /** 日付差分 */
    private long date_between(String strDate1, String strDate2) {
        try {
            LocalDate date1 = LocalDate.of(
                Integer.parseInt(strDate1.substring(0,4)), 
                Integer.parseInt(strDate1.substring(4,6)), 
                Integer.parseInt(strDate1.substring(6,8))
            );
            LocalDate date2 = LocalDate.of(
                Integer.parseInt(strDate2.substring(0,4)), 
                Integer.parseInt(strDate2.substring(4,6)), 
                Integer.parseInt(strDate2.substring(6,8))
            );
            long localDiffDays1 = ChronoUnit.DAYS.between(date1, date2);
            return localDiffDays1;
        } catch (Exception e) {
            return 0;
        }
    }

}
