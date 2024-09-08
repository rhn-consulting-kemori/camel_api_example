package com.redhat.example.config;

import lombok.Data;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import com.redhat.example.entity.SeikyuSimpleUnitEntity;
import com.redhat.example.entity.SaikenSimpleUnitEntity;

@Component
@Data
public class AppConfig {
    
    /** Config Parameter */
    private SeikyuSimpleUnitEntity sp1_seikyu;
    private SeikyuSimpleUnitEntity sprv_seikyu;
    private SaikenSimpleUnitEntity sp1_zandaka;
    private SaikenSimpleUnitEntity sprv_zandaka;
    
    /** コンストラクター */
    public AppConfig() {
        sp1_seikyu = new SeikyuSimpleUnitEntity(BigDecimal.valueOf(50000), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        sprv_seikyu = new SeikyuSimpleUnitEntity(BigDecimal.valueOf(30000), BigDecimal.valueOf(369), BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        sp1_zandaka = new SaikenSimpleUnitEntity(BigDecimal.valueOf(50000), BigDecimal.valueOf(0));
        sprv_zandaka = new SaikenSimpleUnitEntity(BigDecimal.valueOf(30000), BigDecimal.valueOf(0));
    }

}
