package com.bootcamp18.training.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDetailUserDto {
    private String fullName;
    private String phoneNumber;
}
