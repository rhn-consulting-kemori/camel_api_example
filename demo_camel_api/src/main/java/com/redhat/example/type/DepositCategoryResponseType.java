package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class DepositCategoryResponseType extends ServiceResponseType {

    /** 入金種類区分 */
    private String deposit_category_code;

}