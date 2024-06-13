package com.redhat.example.beans;

import org.springframework.stereotype.Component;
import com.redhat.example.types.ResponseType;

@Component
public class GetBean {

    public ResponseType sayHello() {
        // Your logic can go here! If you return a POJO, Camel will try and serialise it to JSON.
        return new ResponseType("Hello, world!");
    }

}