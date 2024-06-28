package com.redhat.example.entity;

import java.util.Map;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SeikyuCompositeUnitEntity {

    /** 合計 */
    private SeikyuSimpleUnitEntity total_billing;

    /** 商品単位 */
    private Map<String, SeikyuSimpleUnitEntity> products_billing_map;
    
}
