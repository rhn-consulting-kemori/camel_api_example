package com.redhat.example.beans;

import org.springframework.stereotype.Component;
import com.redhat.example.types.CardCheckResponseType;

@Component
public class CardCheckBean {

    public CardCheckResponseType checkCard(String customerid, String cardnumber) {
        
        CardCheckResponseType responseType = new CardCheckResponseType(customerid, cardnumber);

        // Rule
        responseType.setCheck_result("1");
        
        return responseType;
    }

}