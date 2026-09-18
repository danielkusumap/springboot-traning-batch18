package com.bootcamp18.gatewayservice.dto.constant;

import java.util.List;

public class ConstantVariable {
    public static final List<String> PUBLIC_ENDPOINT = List.of(
            "/rest/v1/auth/login",
            "/rest/v1/user/register"
    );
}
