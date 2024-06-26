package com.redhat.example;

// Camel
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

// Spring
import org.springframework.stereotype.Component;

// Business Object
import com.redhat.example.type.DepositEntryCheckRequestType;
import com.redhat.example.type.DepositEntryCheckResponseType;
import com.redhat.example.type.DepositCategoryRequestType;
import com.redhat.example.type.DepositCategoryResponseType;

// Rule
import com.redhat.example.rule.DepositEntryCheckRule;
import com.redhat.example.rule.DepositCategoryRule;

@Component
public class RestDslRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Configuration
        restConfiguration()
            .component("servlet")                   // Use the 'servlet' component.
            .bindingMode(RestBindingMode.auto);     //Allow Camel to try to marshal/unmarshal between Java objects and JSON

        // Routing
        rest("/demo")
            .post("/deposit-entry-check").consumes("application/json").produces("application/json").type(DepositEntryCheckRequestType.class).outType(DepositEntryCheckResponseType.class)
                .to("direct:deposit-entry-check")
            .post("/deposit-category").consumes("application/json").produces("application/json").type(DepositCategoryRequestType.class).outType(DepositCategoryResponseType.class)
                .to("direct:deposit-category");

        // Camel Route
        from("direct:deposit-entry-check")
            .bean(DepositEntryCheckRule.class, "check");
        from("direct:deposit-category")
            .bean(DepositCategoryRule.class, "categorise");

    }
}