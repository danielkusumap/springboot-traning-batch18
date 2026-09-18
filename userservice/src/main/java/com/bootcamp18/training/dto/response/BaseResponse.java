package com.bootcamp18.training.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    @Builder.Default
    private UUID reqId = UUID.randomUUID();

    @Builder.Default
    private String status = "T";

    @Builder.Default
    private String message = "Succuss";

    private T data;
}
