package com.abdelrahman.appointmentscheduling.exception;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomAttribute extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            WebRequest webRequest,
            ErrorAttributeOptions options) {

        Map<String, Object> attributes =
                super.getErrorAttributes(webRequest, options);

        String message = (String) attributes.get("message");
        Object status = attributes.get("status");

        attributes.put("errorMessage", message);
        attributes.put("statusCode", status);

        return attributes;
    }
}
