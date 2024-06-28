package com.redhat.example.rule;

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
import com.redhat.example.type.DepositRequestType;
import com.redhat.example.type.DepositResponseType;
import com.redhat.example.entity.DepositDataEntity;
import com.redhat.example.entity.SaikenCompositeUnitEntity;
import com.redhat.example.entity.SaikenSimpleUnitEntity;
@Component
public class DepositRule {

    @Autowired
    private AppConfig appConfig;

    public void deposit(Exchange exchange) {

        /**
         * Exchange IN
         */
        DepositRequestType request_body = exchange.getMessage().getBody(DepositRequestType.class);

        // Set Request
        DepositResponseType exchange_message = new DepositResponseType();
        exchange_message.setService_request(request_body);
        exchange_message.setResult("0", "", "");

        DepositDataEntity deposit_data = new DepositDataEntity();
        deposit_data.setDeposit_allocation_amount(request_body.getDeposit_allocation_data().getDeposit_allocation_amount());
        deposit_data.setExcess_money(request_body.getDeposit_allocation_data().getExcess_money());
        deposit_data.setEstimated_billing_amount(request_body.getDeposit_allocation_data().getEstimated_billing_amount());

        Map<String, SaikenSimpleUnitEntity> products_balance_map = new HashMap();
        products_balance_map.put("sp1", appConfig.getSp1_zandaka());
        products_balance_map.put("sprv", appConfig.getSprv_zandaka());

        SaikenCompositeUnitEntity balance_amount = new SaikenCompositeUnitEntity();
        balance_amount.setTotal_amout(summaryProduct(products_balance_map));
        balance_amount.setProducts_amount_map(products_balance_map);
        deposit_data.setBalance_amount(balance_amount);

        exchange_message.setDeposit_data(deposit_data);

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }

    /** 残高合計額の算出 */
    public SaikenSimpleUnitEntity summaryProduct(Map<String, SaikenSimpleUnitEntity> products_map) {
        SaikenSimpleUnitEntity total = new SaikenSimpleUnitEntity();
        for(Map.Entry<String, SaikenSimpleUnitEntity> entry: products_map.entrySet()) {
            total.setPrincipal_amount(total.getPrincipal_amount().add(entry.getValue().getPrincipal_amount()));
            total.setInterest_amount(total.getInterest_amount().add(entry.getValue().getInterest_amount()));
        }
        return total;
    }
}
