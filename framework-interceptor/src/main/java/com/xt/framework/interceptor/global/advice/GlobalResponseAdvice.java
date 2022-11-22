package com.xt.framework.interceptor.global.advice;

import cn.hutool.core.util.ObjectUtil;
import com.xt.framework.db.api.dto.LogInfo;
import com.xt.framework.interceptor.global.localValue.RequestHolder;
import com.xt.framwork.common.core.bean.Response;
import com.xt.framwork.common.core.bean.ResultResponse;
import com.xt.framwork.common.core.constant.Constants;
import com.xt.framwork.common.core.exception.BizException;
import com.xt.framwork.common.core.exception.ExceptionEnum;
import com.xt.framwork.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * @author tao.xiong
 * @Description controller advice 处理异常
 * @Date 2022/7/11 16:17
 */
@Slf4j
@ControllerAdvice
public class GlobalResponseAdvice<T> implements ResponseBodyAdvice<Object> {
    /**
     * 处理自定义的业务异常
     *
     * @param req 请求
     * @param e   异常
     * @return 结果
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultResponse<T> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("请求：{}发生业务异常！原因是：{}", req.getRequestURI(), e.getErrorMsg());
        return ResultResponse.fail(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResultResponse.fail(ExceptionEnum.METHOD_ARGUMENT_NOT_VALID);
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public Response handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return ResultResponse.fail(ExceptionEnum.VALIDATION);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return ResultResponse.fail(ExceptionEnum.DUPLICATE_KEY);
    }


    /**
     * 处理空指针的异常
     *
     * @param req 请求
     * @param e   异常
     * @return 结果
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse<T> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("请求：{}发生空指针异常！原因是:", req.getRequestURI(), e);
        return ResultResponse.fail(ExceptionEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     *
     * @param req 请求
     * @param e   异常
     * @return 结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse<T> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("请求：{}未知异常！原因是:", req.getRequestURI(), e);
        return ResultResponse.fail(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean supports(@Nonnull MethodParameter methodParameter, @Nonnull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, @Nonnull MethodParameter methodParameter, @Nonnull MediaType mediaType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> aClass, @Nonnull ServerHttpRequest serverHttpRequest, @Nonnull ServerHttpResponse serverHttpResponse) {
        LogInfo logInfo = RequestHolder.getLogInfoThreadLocal();
        int gussErrorLength = 100;
        if (null != logInfo && ObjectUtil.length(o) < gussErrorLength) {
            // 设置返回结果，这里拿到的是controller方法的返回值
            logInfo.setReturnData(null == o ? "" : JsonUtils.encode(o));
        }
        if (o instanceof Response) {
            Response responseVo = (Response) o;
            HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            Object traceId = request.getAttribute(Constants.TRACE_ID);
            Object requestId = request.getAttribute(Constants.REQUEST_ID);
            responseVo.setRequestId(String.valueOf(requestId));
            responseVo.setTraceId(String.valueOf(traceId));
            return responseVo;
        }
        return o;
    }
}
