package com.redhat.example.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper=true)
@Component
public class CardCheckRequestType extends ServiceRequestType {

    /** カード番号 */
    private String cardnumber;
    
    /** コンストラクタ */
    public CardCheckRequestType() {}
    public CardCheckRequestType(String request_id, String cardnumber) {
        super.setRequest_id(request_id);
        this.setCardnumber(cardnumber);
    }
}
