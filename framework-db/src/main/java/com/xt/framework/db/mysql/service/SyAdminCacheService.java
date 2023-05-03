package com.xt.framework.db.mysql.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xt.framework.db.cache.AbstractCache;
import com.xt.framework.db.mysql.mapper.model.SyAdmin;
import com.xt.framework.db.mysql.service.dto.SyAdminInfo;
import com.xt.framwork.common.core.util.BeanUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author tao.xiong
 * @Description 缓存
 * @Date 2022/11/30 14:57
 */
@Component
public class SyAdminCacheService extends AbstractCache<Long, SyAdminInfo> {
    @Resource
    private ISyAdminService syAdminService;

    @Override
    public String getKey() {
        return "sy_admin";
    }

    @Override
    public void initBloomFilter() {
       RBloomFilter<Long> bloomFilter= getBloomFilter();
        syAdminService.list().forEach(syAdmin -> bloomFilter.add(syAdmin.getUserId()));
    }

    @Override
    public SyAdminInfo willCacheData(Long userId) {
        SyAdmin syAdmin = syAdminService.getOne(Wrappers.<SyAdmin>lambdaQuery().eq(SyAdmin::getUserId, userId));
        if (syAdmin == null) {
            return null;
        }
        SyAdminInfo syAdminInfo=new SyAdminInfo();
        BeanUtil.copyProperties(syAdmin,syAdminInfo);
        syAdminInfo.setLogicExpireTime(LocalDateTime.now().plusHours(1L));
        return syAdminInfo;
    }
}
