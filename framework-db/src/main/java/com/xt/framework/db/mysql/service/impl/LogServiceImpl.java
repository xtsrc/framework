package com.xt.framework.db.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.db.mysql.mapper.LogMapper;
import com.xt.framework.db.mysql.mapper.model.Log;
import com.xt.framework.db.mysql.service.ILogService;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 日志服务
 * @Date 2022/10/6 17:26
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
}
