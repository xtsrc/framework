package com.xt.framework.common.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author tao.xiong
 * @Description TODO
 * @Date 2021/7/28 17:57
 */
@ApiModel(
        description = "分页列表"
)
public class PageInfo<T> extends Chunk<T> {
    private static final long serialVersionUID = 2078714870927131024L;
    @ApiModelProperty(
            name = "count",
            value = "总页数",
            dataType = "int"
    )
    private int count;

    public PageInfo() {
    }

    public PageInfo(int count, List<T> rows) {
        this.count = count;
        this.setRows(rows);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
