package com.xt.framework.demo.concurrent.division.async.aggregator;

import io.github.lvyahui8.spring.facade.DataFacade;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author tao.xiong
 * @date 2023/4/11 18:12
 * @desc
 */
@Service
public class DataService {
    public User query(Long userId) {
        try {
            return DataFacade.get("userFullData", Collections.singletonMap("userId", userId), User.class);
        } catch (Exception e) {
            return null;
        }
    }
}
