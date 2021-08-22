package com.ozragwort.moaon.springboot.dto.apiResult;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResult {

    boolean success;
    Object response;
    Object error;

    public ApiResult succeed(Object response) {
        this.success = true;
        this.response = response;
        this.error = null;
        return this;
    }

    public ApiResult failed(Object error) {
        this.success = false;
        this.response = null;
        this.error = error;
        return this;
    }

}
