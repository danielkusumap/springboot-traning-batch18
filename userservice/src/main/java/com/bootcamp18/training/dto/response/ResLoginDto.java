package com.bootcamp18.training.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResLoginDto {
    private String token;
    private String email;
}
