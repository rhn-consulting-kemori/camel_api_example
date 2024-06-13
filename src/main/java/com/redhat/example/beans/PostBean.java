package com.redhat.example.beans;

import com.redhat.example.types.PostRequestType;
import org.springframework.stereotype.Component;
import com.redhat.example.types.ResponseType;

@Component
public class PostBean {

    public ResponseType response(PostRequestType input) {
        // We create a new object of the ResponseType
        // Camel will be able to serialise this automatically to JSON
        return new ResponseType("Thanks for your post, " + input.getName() + "!");
    }
}