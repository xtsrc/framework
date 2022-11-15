package com.xt.framework.demo.designmode.chain.filter;

import com.xt.framework.demo.designmode.chain.FilterContext;
import com.xt.framework.demo.designmode.chain.IFilter;
import com.xt.framework.demo.designmode.chain.IRequest;
import com.xt.framework.demo.designmode.chain.IResponse;
import org.springframework.stereotype.Service;

/**
 * @Author: tao.xiong
 * @Date: 2019/12/20 14:19
 * @Description:
 */
@Service
public class ParamFilter implements IFilter {
    @Override
    public void doFilter(IRequest request, IResponse response, FilterContext context) throws RuntimeException {

    }
}
