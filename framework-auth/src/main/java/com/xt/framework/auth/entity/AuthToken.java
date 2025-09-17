package com.xt.framework.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author tao.xiong
 * @Description token
 * @Date 2022/7/26 17:36
 */
@Data
@ToString
@NoArgsConstructor
public class AuthToken {
    String accessToken;
    String refreshToken;
    String jwtToken;
}