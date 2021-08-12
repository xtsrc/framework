package com.xt.framework.common.core.bean;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author tao.xiong
 * @Description TODO
 * @Date 2021/7/28 18:21
 */
public class ResultBean<T> extends Response {
    private static final long serialVersionUID = -6394556731977143569L;
    private static final ResultBean EMPTY_OK_RESULT = new ResultBean();
    @ApiModelProperty("业务数据")
    private T data;

    public static final ResultBean success() {
        return EMPTY_OK_RESULT;
    }

    public static ResultBean error(int code, String mes) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(code);
        resultBean.setMes(mes);
        return resultBean;
    }

    public static <T> ResultBean success(T data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        resultBean.setMes("success");
        resultBean.setData(data);
        return resultBean;
    }

    @Override
    public String toString() {
        return "ResultBean{data=" + this.data + ", code=" + super.getCode() + ", mes='" + super.getMes() + '\'' + ", requestId='" + super.getRequestId() + '\'' + '}';
    }

    public ResultBean() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResultBean)) {
            return false;
        } else {
            ResultBean<?> other = (ResultBean) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ResultBean;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $data = this.getData();
        return (result * 59 + ($data == null ? 43 : $data.hashCode())) * 59 + ($data == null ? 43 : $data.hashCode());
    }
}
