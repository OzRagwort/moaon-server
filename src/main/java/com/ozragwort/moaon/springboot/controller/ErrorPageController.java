package com.ozragwort.moaon.springboot.controller;

import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.apiResult.FailedResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping(path = "/error", produces = "application/json")
    public ResponseEntity<ApiResult> handleError(HttpServletRequest request) {
        int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Exception exception = (Exception) request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
        String message = exception == null ? "" : exception.getMessage();

        FailedResponse failedResponse = FailedResponse.builder()
                .status(status)
                .message(message)
                .build();
        ApiResult apiResult = new ApiResult().failed(failedResponse);

        if (status == HttpStatus.BAD_REQUEST.value()) {
            // 400
            return ResponseEntity.badRequest()
                    .body(apiResult);
        } else if (status == HttpStatus.NOT_FOUND.value()) {
            // 404
            failedResponse = new FailedResponse(
                    404,
                    "Not Found : " + request.getAttribute("javax.servlet.error.request_uri")
            );
            apiResult = new ApiResult().failed(failedResponse);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(apiResult);
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            // 500
            return ResponseEntity.status(status)
                    .body(apiResult);
        } else {
            // etc
            return ResponseEntity.status(status)
                    .body(apiResult);
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
