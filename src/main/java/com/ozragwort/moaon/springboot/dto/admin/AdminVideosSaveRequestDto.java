package com.ozragwort.moaon.springboot.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminVideosSaveRequestDto {

    @NotNull(message = "videoId must be provided")
    private String videoId;

}
