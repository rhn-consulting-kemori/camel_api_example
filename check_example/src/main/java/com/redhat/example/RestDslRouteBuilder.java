package com.redhat.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import org.springframework.stereotype.Component;

import com.redhat.example.type.CardCheckResponseType;
import com.redhat.example.rule.CardCheckRule;

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
            .get("/card-check/{cardnumber}").produces("application/json").outType(CardCheckResponseType.class)
                .param().name("cardnumber").type(RestParamType.path).description("CARD_NUMBER").dataType("string").endParam()
                .to("direct:card-check");

        // Camel Route
        from("direct:card-check")
            .bean(CardCheckRule.class, "checkCard(${header.cardnumber})");

    }
}
