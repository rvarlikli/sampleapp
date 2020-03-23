package com.product.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.product.util.ErrorCode;

@JsonIgnoreProperties({"stackTrace", "suppressed", "cause", "localizedMessage"})
@JsonPropertyOrder({"message", "errorCode"})
public class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
