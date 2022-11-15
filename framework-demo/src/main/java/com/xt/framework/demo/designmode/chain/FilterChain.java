package com.xt.framework.demo.designmode.chain;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类描述:
 * User: chenggangxu@sohu-inc.com
 * Date: 2016/1/19
 * Time: 13:13
 */
public class FilterChain<R extends IRequest, T extends IResponse> {
    private static final Logger logger = LoggerFactory.getLogger(FilterChain.class);

    private String filterChainName;
    private FilterContext context;
    private List<IFilter<R, T>> filterList;

    public String getFilterChainName() {
        return filterChainName;
    }

    public void setFilterChainName(String filterChainName) {
        this.filterChainName = filterChainName;
    }

    public FilterContext getContext() {
        return context;
    }

    public void setContext(FilterContext context) {
        this.context = context;
    }

    public List<IFilter<R, T>> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<IFilter<R, T>> filterList) {
        this.filterList = filterList;
    }

    public void doExecute(R request, T response) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(response);
        FilterContext context = FilterContext.builder();
        if (CollectionUtils.isNotEmpty(filterList)) {
            for (IFilter<R, T> filter : filterList) {
                if (filter == null) {
                    continue;
                }
                filter.doFilter(request, response, context);
            }
        }
    }

    static class Builder<T extends IRequest, F extends IResponse> {
        private List<IFilter<T, F>> filters = Lists.newArrayList();
        private String filterChainName;

        public static <T extends IRequest, F extends IResponse> Builder<T, F> newBuilder() {
            return new Builder<>();
        }

        public Builder<T, F> addFilter(IFilter<T, F> filter) {
            filters.add(filter);
            return this;
        }

        public Builder<T, F> setChainName(String filterChainName) {
            this.filterChainName = filterChainName;
            return this;
        }

        public FilterChain<T, F> build() {
            Preconditions.checkArgument(CollectionUtils.isNotEmpty(this.filters));
            FilterChain<T, F> filterChain = new FilterChain<>();
            filterChain.setFilterChainName(filterChainName);
            filterChain.setFilterList(ImmutableList.copyOf(filters));

            return filterChain;
        }

    }

}
