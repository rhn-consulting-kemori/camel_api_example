package com.redhat.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import org.springframework.stereotype.Component;
import com.redhat.example.types.CardCheckResponseType;
import com.redhat.example.beans.CardCheckBean;

@Component
public class RestDslRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Configuration
        restConfiguration()
            .component("servlet")                   // Use the 'servlet' component.
            .bindingMode(RestBindingMode.auto);     //Allow Camel to try to marshal/unmarshal between Java objects and JSON

        // Routing
        rest("/mock")
            .get("/card-check/{customerid}").produces("application/json").outType(CardCheckResponseType.class)
                .param().name("customerid").type(RestParamType.path).description("CUSTOMER_CONTRACT_NUMBER").dataType("string").endParam()
                .param().name("cardnumber").type(RestParamType.query).description("CARD_NUMBER").endParam()
                .to("direct:card-check");

        // Camel Route
        from("direct:card-check")
            .bean(CardCheckBean.class, "checkCard(${header.customerid}, ${body.cardnumber})");

    }
}
