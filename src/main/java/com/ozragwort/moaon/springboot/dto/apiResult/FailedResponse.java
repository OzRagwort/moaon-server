package com.ozragwort.moaon.springboot.dto.apiResult;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FailedResponse {

    private int status;
    private String message;

    @Builder
    public FailedResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
