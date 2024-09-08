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
import com.redhat.example.type.CheckAvailableDepositAmountRequestType;
import com.redhat.example.type.CheckAvailableDepositAmountResponseType;
import com.redhat.example.type.DepositAllocationRequestType;
import com.redhat.example.type.DepositAllocationResponseType;
import com.redhat.example.type.DepositRequestType;
import com.redhat.example.type.DepositResponseType;
import com.redhat.example.type.KijitsuAllocationDepositRequestType;
import com.redhat.example.type.KijitsuAllocationDepositResponseType;

// Rule
import com.redhat.example.rule.DepositEntryCheckRule;
import com.redhat.example.rule.DepositCategoryRule;
import com.redhat.example.rule.CheckAvailableDepositAmountRule;
import com.redhat.example.rule.DepositAllocationRule;
import com.redhat.example.rule.DepositRule;

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
                .to("direct:deposit-category")
            .post("/check-available-deposit-amount").consumes("application/json").produces("application/json").type(CheckAvailableDepositAmountRequestType.class).outType(CheckAvailableDepositAmountResponseType.class)
                .to("direct:check-available-deposit-amount")
            .post("/deposit-allocation").consumes("application/json").produces("application/json").type(DepositAllocationRequestType.class).outType(DepositAllocationResponseType.class)
                .to("direct:deposit-allocation")
            .post("/deposit").consumes("application/json").produces("application/json").type(DepositRequestType.class).outType(DepositResponseType.class)
                .to("direct:deposit")
            .post("/kijitsu-allocation-deposit").consumes("application/json").produces("application/json").type(KijitsuAllocationDepositRequestType.class).outType(KijitsuAllocationDepositResponseType.class)
                .to("direct:kijitsu-allocation-deposit");

        // Camel Route
        from("direct:deposit-entry-check")
            .bean(DepositEntryCheckRule.class, "check");
        from("direct:deposit-category")
            .bean(DepositCategoryRule.class, "categorise");
        from("direct:check-available-deposit-amount")
            .bean(CheckAvailableDepositAmountRule.class, "simulate");
        from("direct:deposit-allocation")
            .bean(DepositAllocationRule.class, "simulate");
        from("direct:deposit")
            .bean(DepositRule.class, "deposit");
        from("direct:kijitsu-allocation-deposit")
            .process("kijitsuAllocationDepositProcessor");

    }
}
