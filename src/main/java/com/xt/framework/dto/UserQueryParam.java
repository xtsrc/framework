package com.xt.framework.dto;

import com.xt.framework.common.core.bean.BatchRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author tao.xiong
 * @Description TODO
 * @Date 2021/7/28 18:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
public class UserQueryParam extends BatchRequest<UserQueryParam> {
    private static final long serialVersionUID = -5765491479708976561L;
    @ApiModelProperty("模糊查询用户")
    private String user;
}
