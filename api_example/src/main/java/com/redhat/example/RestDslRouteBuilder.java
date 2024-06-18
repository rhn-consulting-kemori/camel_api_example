package com.redhat.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import com.redhat.example.types.PostRequestType;
import com.redhat.example.types.ResponseType;

@Component
public class RestDslRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Configuration
        restConfiguration()
            .component("servlet")                   // Use the 'servlet' component.
            .bindingMode(RestBindingMode.auto);     //Allow Camel to try to marshal/unmarshal between Java objects and JSON

        // Routing
        rest("/say")
            .get("/hello").produces("application/json").outType(ResponseType.class).to("direct:hello")
            .post("/bye").consumes("application/json").produces("application/json").type(PostRequestType.class).outType(ResponseType.class).to("direct:bye");

        // Camel Route
        from("direct:hello")
            .to("bean:getBean");
        from("direct:bye")
            .to("bean:postBean");

    }
}
