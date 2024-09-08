package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.stereotype.Component;

import com.redhat.example.type.ServiceRequestType;
import com.redhat.example.type.ServiceResponseType;
import com.redhat.example.entity.DepositDataEntity;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class KijitsuAllocationDepositResponseType extends ServiceResponseType {

    /** 入金結果情報 */
    private DepositDataEntity deposit_data;

}
