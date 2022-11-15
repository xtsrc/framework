package com.xt.framework.demo.designmode.chain;

import javax.validation.constraints.NotNull;

/**
 * 类描述:过滤器 接口
 * User: chenggangxu@sohu-inc.com
 * Date: 2016/1/19
 * Time: 13:09
 */
public interface IFilter<R extends IRequest, T extends IResponse> {

    /**
     * 责任链方法
     */
    void doFilter(@NotNull R request, T response, FilterContext context) throws RuntimeException;

}
