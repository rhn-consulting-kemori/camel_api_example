package com.redhat.example.entity;

import java.util.Map;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SaikenCompositeUnitEntity {

    /** 合計 */
    private SaikenSimpleUnitEntity total_amout;

    /** 商品単位 */
    private Map products_amount_map;
    
}
