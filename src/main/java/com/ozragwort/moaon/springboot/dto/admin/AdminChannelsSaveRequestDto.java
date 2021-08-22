package com.ozragwort.moaon.springboot.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminChannelsSaveRequestDto {

    @NotNull(message = "categoryId must be provided")
    private Long categoryId;

    @NotNull(message = "channelId must be provided")
    private String channelId;

}
