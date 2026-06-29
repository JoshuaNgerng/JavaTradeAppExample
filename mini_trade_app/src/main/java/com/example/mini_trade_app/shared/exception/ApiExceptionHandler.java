package com.example.mini_trade_app.shared.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatusCode;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handle(BusinessException ex) {

        HttpStatusCode status = HttpStatusCode.valueOf(
            ex.getErrorCode().status()
        );
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(
                ex.getErrorCode().code(),
                ex.getMessage()
            ));
    }
}
