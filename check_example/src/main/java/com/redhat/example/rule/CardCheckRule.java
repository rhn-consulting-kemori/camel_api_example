package com.redhat.example.rule;

import org.springframework.stereotype.Component;
import com.redhat.example.type.CardCheckResponseType;

@Component
public class CardCheckRule {

    /** カード番号 */
    private String cardnumber;

    public CardCheckResponseType checkCard(String cardnumber) {
        
        this.cardnumber = cardnumber;

        CardCheckResponseType responseType = new CardCheckResponseType(cardnumber);

        // Rule
        if(!check_length()) {
            responseType.setResult("1", "E01", "length error");
            return responseType;
        }

        if(!check_numerical()){
            responseType.setResult("1", "E02", "numeric error");
            return responseType;
        }

        if(!check_brand()){
            responseType.setResult("1", "E03", "brand error");
            return responseType;
        }

        responseType.setResult("0", "", "");
        return responseType;

    }

    /** 桁数チェック */
    private boolean check_length(){
        if(this.cardnumber.length() == 16) {
            return true;
        } else {
            return false;
        }
    }

    /** 数値チェック */
    private boolean check_numerical(){
        return this.cardnumber.matches("[+-]?\\d*(\\.\\d+)?");
    }

    /** ブランドチェック */
    private boolean check_brand(){
        if(this.cardnumber.substring(0,2).equals("35")) {
            return true;
        } else {
            return false;
        }
    }

}