package com.redhat.example.type;

import lombok.Data;

@Data
public class ServiceResponseType {

    /** Service Request Type */
    private ServiceRequestType service_request;

    /** 結果 */
    private String response_result;
    
    /** エラーコード */
    private String err_code;
        
    /** エラー内容 */
    private String err_context;

    /** エラー設定 */
    public void setResult(String response_result, String err_code, String err_context) {
        this.response_result = response_result;
        this.err_code = err_code;
        this.err_context = err_context;
    }

}
