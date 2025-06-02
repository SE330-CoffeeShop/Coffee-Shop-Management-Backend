package com.se330.coffee_shop_management_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
public abstract class AbstractBaseResponse {

    protected int statusCode;
    protected String message;

    protected AbstractBaseResponse() {
    }
}
