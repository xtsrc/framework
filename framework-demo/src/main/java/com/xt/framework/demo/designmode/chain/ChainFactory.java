package com.xt.framework.demo.designmode.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 类描述:
 * User: chenggangxu@sohu-inc.com
 * Date: 2016/10/31
 * Time: 11:25
 */
@Service
public class ChainFactory {
    private final static Logger logger = LoggerFactory.getLogger(ChainFactory.class);

    private FilterChain<IRequest, IResponse> actionCompleteChain;
    private FilterChain<IRequest, IResponse> preCheckChain;

    @Resource
    private IFilter<IRequest, IResponse> paramFilter;
    @Resource
    private IFilter<IRequest, IResponse> eventFilter;


    @PostConstruct
    private void init() {
        this.preCheckChain = FilterChain.Builder.newBuilder()
                .addFilter(paramFilter)
                .setChainName("preCheckChain")
                .build();

        this.actionCompleteChain = FilterChain.Builder.newBuilder()
                .addFilter(eventFilter)
                .setChainName("actionCompleteChain")
                .build();
    }

    public FilterChain<IRequest, IResponse> getPreCheckChain() {
        return preCheckChain;
    }

    public FilterChain<IRequest, IResponse> getActionCompleteChain() {
        return actionCompleteChain;
    }
}
