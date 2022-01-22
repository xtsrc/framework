package com.xt.framwork.common.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 17:58
 */
public class Chunk<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 3115793056130748662L;
    @ApiModelProperty(
            name = "rows",
            value = "数据列表",
            dataType = "List"
    )
    private List<T> rows;

    public Chunk() {
    }

    public Chunk(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public Iterator<T> iterator() {
        return this.rows == null ? Collections.emptyIterator() : this.rows.iterator();
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @JsonIgnore
    public int getRowsSize() {
        return this.rows != null ? this.rows.size() : 0;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Chunk)) {
            return false;
        } else {
            Chunk<?> that = (Chunk) obj;
            return this.rows.equals(that.rows);
        }
    }
    @Override
    public int hashCode() {
        int result = 17;
        return result + 31 * this.rows.hashCode() + 31 * this.rows.hashCode();
    }
}
