package com.xt.framework.interceptor.global;

import com.xt.framwork.common.core.bean.ResultResponse;
import com.xt.framwork.common.core.constant.Constants;
import com.xt.framwork.common.core.exception.BizException;
import com.xt.framwork.common.core.exception.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tao.xiong
 * @Description 全局处理
 * @Date 2022/7/11 16:17
 */
@Slf4j
@ControllerAdvice
public class GlobalHandler<T> implements ResponseBodyAdvice<Object> {
    /**
     * 处理自定义的业务异常
     * @param req 请求
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultResponse<T> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultResponse.fail(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param req 请求
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse<T> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResultResponse.fail(ExceptionEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     *
     * @param req 请求
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse<T> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        return ResultResponse.fail(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ResultResponse<T> responseVo = (ResultResponse<T>)o ;
        HttpServletRequest request= ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        Object traceId=request.getAttribute(Constants.TRACE_ID);
        Object requestId=request.getAttribute(Constants.REQUEST_ID);
        responseVo.setRequestId(String.valueOf(requestId));
        responseVo.setTraceId(String.valueOf(traceId));
        return responseVo;
    }
}
