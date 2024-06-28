package com.redhat.example.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

// Camel
import org.apache.camel.Exchange;

// Spring
import org.springframework.stereotype.Component;

// Business Object
import com.redhat.example.type.DepositAllocationRequestType;
import com.redhat.example.type.DepositAllocationResponseType;
import com.redhat.example.entity.DepositAllocationDataEntity;
import com.redhat.example.entity.AvailableDepositAmountDataEntity;
import com.redhat.example.entity.SaikenCompositeUnitEntity;
import com.redhat.example.entity.SaikenSimpleUnitEntity;
import com.redhat.example.entity.SeikyuCompositeUnitEntity;
import com.redhat.example.entity.SeikyuSimpleUnitEntity;

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
        SeikyuCompositeUnitEntity seikyu_amount = copySeikyuComposite(request_body.getDeposit_available_amount_data().getEstimated_billing_amount());
        SaikenCompositeUnitEntity deposit_available = copyDepositComposite(request_body.getDeposit_available_amount_data().getDeposit_available_amount());
        BigDecimal deposit_amount = request_body.getDeposit_amount();
        BigDecimal principal_amount = deposit_available.getTotal_amout().getPrincipal_amount();
        BigDecimal interest_amount = deposit_available.getTotal_amout().getInterest_amount();
        BigDecimal sum_amount = principal_amount.add(interest_amount);

        if(deposit_amount.compareTo(sum_amount) >= 0) {
            // 過剰金が発生 / 全額入金
            exchange_message.setDeposit_allocation_data(excessAllocation(seikyu_amount, deposit_available, deposit_amount, sum_amount));
        } else {
            // 一部入金
            exchange_message.setDeposit_allocation_data(
                partialAllocation(seikyu_amount, deposit_available, deposit_amount, sum_amount, principal_amount, interest_amount, 
                    request_body.getDeposit_date(), request_body.getContract_settlement_date()));
        }

        /**
         * Exchange OUT
         */
        exchange.getMessage().setBody(exchange_message);

    }

    // 過剰金が発生
    public DepositAllocationDataEntity excessAllocation(
        SeikyuCompositeUnitEntity seikyu_amount, SaikenCompositeUnitEntity deposit_available, BigDecimal deposit_amount, BigDecimal sum_amount) {
        
        DepositAllocationDataEntity allocation_data = new DepositAllocationDataEntity();

        // 入金充当額
        allocation_data.setDeposit_allocation_amount(deposit_available);

        // 請求額再設定
        allocation_data.setEstimated_billing_amount(wholeAllocationSeiku(seikyu_amount, deposit_available));

        // 過剰金
        allocation_data.setExcess_money(deposit_amount.subtract(sum_amount));

        return allocation_data;

    }

    // 全額入金請求額計算
    public SeikyuCompositeUnitEntity wholeAllocationSeiku(SeikyuCompositeUnitEntity seikyu_amount, SaikenCompositeUnitEntity deposit_available) {

        SeikyuCompositeUnitEntity reseikyu_entity = new SeikyuCompositeUnitEntity();

        // Total
        SeikyuSimpleUnitEntity total_bill = seikyu_amount.getTotal_billing();
        SaikenSimpleUnitEntity total_deposit = deposit_available.getTotal_amout();
        total_bill.setBilling_principal_amount(BigDecimal.ZERO);
        total_bill.setBilling_interest_amount(BigDecimal.ZERO);
        total_bill.setDeposit_principal_amount(total_bill.getDeposit_principal_amount().add(total_deposit.getPrincipal_amount()));
        total_bill.setDeposit_interest_amount(total_bill.getDeposit_interest_amount().add(total_deposit.getInterest_amount()));
        reseikyu_entity.setTotal_billing(total_bill);

        // Product
        Map<String, SeikyuSimpleUnitEntity> product_bill_map = seikyu_amount.getProducts_billing_map();
        Map<String, SaikenSimpleUnitEntity> product_deposit_map = deposit_available.getProducts_amount_map();
        SeikyuSimpleUnitEntity product_bill_sp1 = product_bill_map.get("sp1");
        SaikenSimpleUnitEntity product_deposit_sp1 = product_deposit_map.get("sp1");
        product_bill_sp1.setBilling_principal_amount(BigDecimal.ZERO);
        product_bill_sp1.setBilling_interest_amount(BigDecimal.ZERO);
        product_bill_sp1.setDeposit_principal_amount(
            product_bill_sp1.getDeposit_principal_amount().add(product_deposit_sp1.getPrincipal_amount()));
        product_bill_sp1.setDeposit_interest_amount(
            product_bill_sp1.getDeposit_interest_amount().add(product_deposit_sp1.getInterest_amount()));

        SeikyuSimpleUnitEntity product_bill_sprv = product_bill_map.get("sprv");
        SaikenSimpleUnitEntity product_deposit_sprv = product_deposit_map.get("sprv");
        product_bill_sprv.setBilling_principal_amount(BigDecimal.ZERO);
        product_bill_sprv.setBilling_interest_amount(BigDecimal.ZERO);
        product_bill_sprv.setDeposit_principal_amount(
            product_bill_sprv.getDeposit_principal_amount().add(product_deposit_sprv.getPrincipal_amount()));
        product_bill_sprv.setDeposit_interest_amount(
            product_bill_sprv.getDeposit_interest_amount().add(product_deposit_sprv.getInterest_amount()));

        reseikyu_entity.setProducts_billing_map(product_bill_map);
        return reseikyu_entity;
    }

    // 一部入金
    public DepositAllocationDataEntity partialAllocation(
        SeikyuCompositeUnitEntity seikyu_amount, SaikenCompositeUnitEntity deposit_available, 
        BigDecimal deposit_amount, BigDecimal sum_amount, BigDecimal principal_amount, BigDecimal interest_amount, 
        String deposit_date, String contract_settlement_date) {
        
        DepositAllocationDataEntity allocation_data = new DepositAllocationDataEntity();
        Map<String, SeikyuSimpleUnitEntity> product_bill_map = seikyu_amount.getProducts_billing_map();
        Map<String, SaikenSimpleUnitEntity> product_deposit_map = deposit_available.getProducts_amount_map();

        if(deposit_amount.compareTo(principal_amount) > 0) {
            // 元本全額充当
            BigDecimal principal_allc = principal_amount;
            BigDecimal interest_allc = deposit_amount.subtract(principal_allc);
            BigDecimal sprv_seikyu_interest = interest_amount;

            // 入金充当額設定
            deposit_available.getTotal_amout().setInterest_amount(interest_allc);
            product_deposit_map.get("sprv").setInterest_amount(interest_allc);
            allocation_data.setDeposit_allocation_amount(deposit_available);

            // 請求額再設定
            seikyu_amount.getTotal_billing().setBilling_principal_amount(BigDecimal.ZERO);
            seikyu_amount.getTotal_billing().setBilling_interest_amount(sprv_seikyu_interest.subtract(interest_allc));
            seikyu_amount.getTotal_billing().setDeposit_principal_amount(seikyu_amount.getTotal_billing().getDeposit_principal_amount().add(principal_allc));
            seikyu_amount.getTotal_billing().setDeposit_interest_amount(seikyu_amount.getTotal_billing().getDeposit_interest_amount().add(interest_allc));

            BigDecimal sp1_ganpon = product_bill_map.get("sp1").getBilling_principal_amount();
            product_bill_map.get("sp1").setBilling_principal_amount(BigDecimal.ZERO);
            product_bill_map.get("sp1").setDeposit_principal_amount(
                product_bill_map.get("sp1").getDeposit_principal_amount().add(sp1_ganpon)
            );

            BigDecimal sprv_ganpon = product_bill_map.get("sprv").getBilling_principal_amount();
            product_bill_map.get("sprv").setBilling_principal_amount(BigDecimal.ZERO);
            product_bill_map.get("sprv").setDeposit_principal_amount(
                product_bill_map.get("sprv").getDeposit_principal_amount().add(sprv_ganpon)
            );
            product_bill_map.get("sprv").setBilling_interest_amount(sprv_seikyu_interest.subtract(interest_allc));
            product_bill_map.get("sprv").setDeposit_interest_amount(
                product_bill_map.get("sprv").getDeposit_interest_amount().add(interest_allc)
            );
            allocation_data.setEstimated_billing_amount(seikyu_amount);
            allocation_data.setExcess_money(BigDecimal.ZERO);

        } else {
            // 元本一部充当
            BigDecimal principal_allc = deposit_amount;
            BigDecimal interest_allc = BigDecimal.ZERO;
            BigDecimal sprv_principal_allc = product_bill_map.get("sprv").getBilling_principal_amount();
    
            // 入金充当
            deposit_available.getTotal_amout().setPrincipal_amount(principal_allc);
            deposit_available.getTotal_amout().setInterest_amount(BigDecimal.ZERO);

            if(sprv_principal_allc.compareTo(principal_allc) <= 0){
                product_deposit_map.get("sprv").setPrincipal_amount(sprv_principal_allc);
                product_deposit_map.get("sp1").setPrincipal_amount(principal_allc.subtract(sprv_principal_allc));
                product_deposit_map.get("sp1").setInterest_amount(BigDecimal.ZERO);
                product_deposit_map.get("sprv").setInterest_amount(BigDecimal.ZERO);
            } else {
                product_deposit_map.get("sprv").setPrincipal_amount(principal_allc);
                product_deposit_map.get("sp1").setPrincipal_amount(BigDecimal.ZERO);
                product_deposit_map.get("sp1").setInterest_amount(BigDecimal.ZERO);
                product_deposit_map.get("sprv").setInterest_amount(BigDecimal.ZERO);
                sprv_principal_allc = principal_allc;
            }
            allocation_data.setDeposit_allocation_amount(deposit_available);

            // 請求額再設定
            seikyu_amount.getTotal_billing().setBilling_principal_amount(seikyu_amount.getTotal_billing().getBilling_principal_amount().subtract(principal_allc));
            seikyu_amount.getTotal_billing().setDeposit_principal_amount(seikyu_amount.getTotal_billing().getDeposit_principal_amount().add(principal_allc));
            BigDecimal minus_seikyu_interest = recalcurateInterest(deposit_date, contract_settlement_date, sprv_principal_allc);
            BigDecimal re_seikyu_interest = seikyu_amount.getTotal_billing().getBilling_interest_amount().subtract(minus_seikyu_interest);
            seikyu_amount.getTotal_billing().setBilling_interest_amount(re_seikyu_interest);

            product_bill_map.get("sp1").setBilling_principal_amount(
                product_bill_map.get("sp1").getBilling_principal_amount().subtract(
                    product_deposit_map.get("sp1").getPrincipal_amount()
                )
            );
            product_bill_map.get("sp1").setDeposit_principal_amount(
                product_bill_map.get("sp1").getDeposit_principal_amount().add(
                    product_deposit_map.get("sp1").getPrincipal_amount()
                )
            );

            product_bill_map.get("sprv").setBilling_principal_amount(
                product_bill_map.get("sprv").getBilling_principal_amount().subtract(
                    product_deposit_map.get("sprv").getPrincipal_amount()
                )
            );
            product_bill_map.get("sprv").setDeposit_principal_amount(
                product_bill_map.get("sprv").getDeposit_principal_amount().add(
                    product_deposit_map.get("sprv").getPrincipal_amount()
                )
            );
            product_bill_map.get("sprv").setBilling_interest_amount(re_seikyu_interest);
            allocation_data.setEstimated_billing_amount(seikyu_amount);
            allocation_data.setExcess_money(BigDecimal.ZERO);
        }
        return allocation_data;
    }

    /** 入金利息額再計算 */
    public BigDecimal recalcurateInterest(String deposit_date, String contract_settlement_date, BigDecimal zandaka) {
        long between_days = date_between(deposit_date, contract_settlement_date);
        if(between_days < 1) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal day_range = BigDecimal.valueOf(date_between(deposit_date, contract_settlement_date));
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

    public SeikyuCompositeUnitEntity copySeikyuComposite(SeikyuCompositeUnitEntity before) {
        SeikyuCompositeUnitEntity after = new SeikyuCompositeUnitEntity();
        SeikyuSimpleUnitEntity total_after = new SeikyuSimpleUnitEntity(
            before.getTotal_billing().getBilling_principal_amount(), 
            before.getTotal_billing().getBilling_interest_amount(), 
            before.getTotal_billing().getDeposit_principal_amount(), 
            before.getTotal_billing().getDeposit_interest_amount()
        );
        after.setTotal_billing(total_after);

        Map<String, SeikyuSimpleUnitEntity> aftermap = new HashMap();
        Map<String, SeikyuSimpleUnitEntity> beforemap = before.getProducts_billing_map();
        for(Map.Entry<String, SeikyuSimpleUnitEntity> entry: beforemap.entrySet()) {
            SeikyuSimpleUnitEntity after_product = new SeikyuSimpleUnitEntity(
                entry.getValue().getBilling_principal_amount(), 
                entry.getValue().getBilling_interest_amount(), 
                entry.getValue().getDeposit_principal_amount(), 
                entry.getValue().getDeposit_interest_amount()
            );
            aftermap.put(entry.getKey(), after_product);
        }
        after.setProducts_billing_map(aftermap);
        return after;
    }

    public SaikenCompositeUnitEntity copyDepositComposite(SaikenCompositeUnitEntity before) {
        SaikenCompositeUnitEntity after = new SaikenCompositeUnitEntity();
        SaikenSimpleUnitEntity total_after = new SaikenSimpleUnitEntity(
            before.getTotal_amout().getPrincipal_amount(), 
            before.getTotal_amout().getInterest_amount()
        );
        after.setTotal_amout(total_after);

        Map<String, SaikenSimpleUnitEntity> aftermap = new HashMap();
        Map<String, SaikenSimpleUnitEntity> beforemap = before.getProducts_amount_map();
        for(Map.Entry<String, SaikenSimpleUnitEntity> entry: beforemap.entrySet()) {
            SaikenSimpleUnitEntity after_product = new SaikenSimpleUnitEntity(
                entry.getValue().getPrincipal_amount(), 
                entry.getValue().getInterest_amount()
            );
            aftermap.put(entry.getKey(), after_product);
        }
        after.setProducts_amount_map(aftermap);
        return after;
    }
}
