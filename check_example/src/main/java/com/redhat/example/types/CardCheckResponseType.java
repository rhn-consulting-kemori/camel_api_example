package com.redhat.example.types;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CardCheckResponseType {

    private String customerid;
    private String cardnumber;
    private String check_result;
    private String err_code;
    private String err_context;

    public CardCheckResponseType() {
    }

    public CardCheckResponseType(String customerid, String cardnumber) {
        this.customerid = customerid;
        this.cardnumber = cardnumber;
    }

}
