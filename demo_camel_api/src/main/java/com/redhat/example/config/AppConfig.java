package com.redhat.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.redhat.example.entity.SeikyuSimpleUnitEntity;
import com.redhat.example.entity.SaikenSimpleUnitEntity;

@Data
@Configuration
@ConfigurationProperties("app")
public class AppConfig {
    
    /** Config Parameter */
    private SeikyuSimpleUnitEntity sp1_seikyu;
    private SeikyuSimpleUnitEntity sprv_seikyu;
    private SaikenSimpleUnitEntity sp1_zandaka;
    private SaikenSimpleUnitEntity sprv_zandaka;
    
}
