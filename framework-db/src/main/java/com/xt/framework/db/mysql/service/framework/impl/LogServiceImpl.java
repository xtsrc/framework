package com.xt.framework.db.mysql.service.framework.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.db.mysql.mapper.framework.LogMapper;
import com.xt.framework.db.mysql.mapper.framework.model.Log;
import com.xt.framework.db.mysql.service.framework.ILogService;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 日志服务
 * @Date 2022/10/6 17:26
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
}
