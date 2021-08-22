package com.ozragwort.moaon.springboot.controller;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.apiResult.FailedResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {
            IllegalArgumentException.class, // Input value invalid error
            MethodArgumentNotValidException.class, // NotNull error
            DataIntegrityViolationException.class, // sql error
            HttpMessageNotReadableException.class, // input json parse error
            NoSuchElementException.class, // Not found value error
            MissingRequestHeaderException.class // Header missing
    })
    public ResponseEntity<ApiResult> badRequest(Exception e) {
        FailedResponse failedResponse = FailedResponse.builder()
                .status(HTTPResponse.SC_BAD_REQUEST)
                .message(e.getMessage())
                .build();
        ApiResult apiResult = new ApiResult().failed(failedResponse);
        return ResponseEntity.badRequest()
                .body(apiResult);
    }

    // If mediaType is not json
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResult> httpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        FailedResponse failedResponse = FailedResponse.builder()
                .status(415)
                .message(e.getMessage())
                .build();
        ApiResult apiResult = new ApiResult().failed(failedResponse);
        return ResponseEntity.status(415)
                .body(apiResult);
    }

}
