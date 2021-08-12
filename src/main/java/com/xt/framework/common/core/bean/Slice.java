package com.xt.framework.common.core.bean;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author tao.xiong
 * @Description TODO
 * @Date 2021/7/28 18:02
 */
public class Slice <T> extends Chunk<T> {
    public static final Slice<?> EMPTY_SLICE = new Slice();
    private static final long serialVersionUID = -104720810321404679L;
    @ApiModelProperty(
            value = "是否有下页",
            required = true
    )
    private boolean hasNext;

    public Slice() {
    }

    public Slice(List<T> rows, boolean hasNext) {
        this.hasNext = hasNext;
        this.setRows(rows);
    }

    public static final <T> Slice<?> emptySlice() {
        return EMPTY_SLICE;
    }

    public boolean isHasNext() {
        return this.hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public static void main(String[] args) {
    }
}
