package com.redhat.example.type;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CardCheckResponseType {

    private String cardnumber;
    private String check_result;
    private String err_code;
    private String err_context;

    public CardCheckResponseType() {
    }

    public CardCheckResponseType(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public void setResult(String check_result, String err_code, String err_context) {
        this.check_result = check_result;
        this.err_code = err_code;
        this.err_context = err_context;
    }

}
