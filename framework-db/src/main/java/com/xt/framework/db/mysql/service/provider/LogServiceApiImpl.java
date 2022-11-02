package com.xt.framework.db.mysql.service.provider;

import com.xt.framework.db.api.ILogServiceApi;
import com.xt.framework.db.api.dto.LogInfo;
import com.xt.framework.db.mysql.mapper.model.Log;
import com.xt.framework.db.mysql.service.ILogService;
import com.xt.framwork.common.core.bean.ResultResponse;
import com.xt.framwork.common.core.util.ComputerUniqueIdentificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 对外提供日志服务
 * @Date 2022/7/26 11:06
 */
@RestController
@Slf4j
@RequestMapping("/log/api")
public class LogServiceApiImpl implements ILogServiceApi {
    @Resource
    private ILogService logService;

    @Override
    public ResultResponse<Void> save(LogInfo logInfo) {
        log.info("保存日志信息");
        Log log = new Log();
        BeanUtils.copyProperties(logInfo, log);
        String uniqueComputer = ComputerUniqueIdentificationUtil.getComputerUniqueIdentificationString();
        log.setCreateBy(uniqueComputer);
        log.setUpdateBy(uniqueComputer);
        logService.save(log);
        return ResultResponse.success();
    }
}
