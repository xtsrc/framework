package com.xt.framework.db.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志信息
 * @Date 2022/07/26 10:49:00
 * @author tao.xiong
 */
@Data
public class LogInfo implements Serializable {
    @ApiModelProperty("请求唯一标识，可以自定义规则生成")
    private String traceId;

    @ApiModelProperty("请求路径")
    private String uri;

    @ApiModelProperty("请求url上的参数")
    private String queryString;

    @ApiModelProperty("请求方式，GET/POST等")
    private String method;

    @ApiModelProperty("操作说明")
    private String description;

    @ApiModelProperty("请求ip，客户端ip地址")
    private String ip;

    @ApiModelProperty("请求体，请求正文的内容")
    private String body;

    @ApiModelProperty("请求token，登录用户token，登录状态下存在")
    private String token;

    @ApiModelProperty("请求用户id，登录用户id，登录状态下存在")
    private Long userId;

    @ApiModelProperty("返回结果，请求的结果")
    private String returnData;
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;
}
