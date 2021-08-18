package com.xt.framework.common.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author tao.xiong
 * @Description 【批量】请求参数
 * @Date 2021/7/13 13:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class BatchRequest<T extends BatchRequest<?>> implements Serializable {
    private static final long serialVersionUID = 3456058472426862571L;
    @ApiModelProperty("分页大小")
    private int pageSize = 10;
    @ApiModelProperty("第几页")
    private Long page = 1L;
    @ApiModelProperty("创建时间查询起始范围")
    private LocalDate createStartDate;
    @ApiModelProperty("创建时间查询结束范围")
    private LocalDate createEndDate;
    @ApiModelProperty("排序字段")
    private String sortField = "createdAt";
    @ApiModelProperty("排序字段")
    private String sortOrder = "DESC";
    @ApiModelProperty("查询字段")
    private String[] includeFields;

    /**
     * 清除分页
     *
     * @return T
     */
    public T clearPage() {
        this.setPage(null);
        this.setPageSize(0);
        return (T) this;
    }

    /**
     * 清除排序
     *
     * @return T
     */
    public T clearSort() {
        this.setSortOrder("");
        this.setSortField("");
        return (T) this;
    }

}
