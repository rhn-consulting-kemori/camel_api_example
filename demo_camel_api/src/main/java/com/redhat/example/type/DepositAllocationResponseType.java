package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.stereotype.Component;

import com.redhat.example.type.ServiceResponseType;
import com.redhat.example.entity.DepositAllocationDataEntity;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class DepositAllocationResponseType extends ServiceResponseType {

    /** 入金充当情報 */
    private DepositAllocationDataEntity deposit_allocation_data;
    
}
