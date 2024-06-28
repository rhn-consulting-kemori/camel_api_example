package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.stereotype.Component;

import com.redhat.example.type.ServiceResponseType;
import com.redhat.example.entity.AvailableDepositAmountDataEntity;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class CheckAvailableDepositAmountResponseType extends ServiceResponseType {

    /** 入金可能額 */
    private AvailableDepositAmountDataEntity deposit_available_amount_data;
    
}
