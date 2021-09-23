package com.xt.framework.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 18:11
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class UserDto implements Serializable {
    private static final long serialVersionUID = -5765491479708976561L;
    @ApiModelProperty("模糊查询用户")
    private String name;
    private Long id;
}
