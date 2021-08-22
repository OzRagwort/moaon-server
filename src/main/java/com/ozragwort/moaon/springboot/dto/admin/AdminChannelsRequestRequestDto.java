package com.ozragwort.moaon.springboot.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminChannelsRequestRequestDto {

    @NotNull(message = "appName must be provided")
    private String appName;

    @NotNull(message = "content must be provided")
    private String content;

}
