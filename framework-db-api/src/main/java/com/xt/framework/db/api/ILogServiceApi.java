package com.xt.framework.db.api;

import com.xt.framework.db.api.dto.LogInfo;
import com.xt.framwork.common.core.bean.ResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tao.xiong
 * @Description 日志服务
 * @Date 2022/7/26 10:40
 */
@Component
@FeignClient(value = "framework-db")
public interface ILogServiceApi {
    /**
     * 日志保存
     *
     * @param logInfo 日志信息
     * @return 保存结果
     */
    @PostMapping("/db/saveLogInfo")
    ResultResponse<Void> save(@RequestBody LogInfo logInfo);
}
