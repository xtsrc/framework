package com.xt.framework.service;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import com.xt.framework.dto.ResponseBean;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author tao.xiong
 * @Description api初始化
 * @Date 2021/8/7 14:57
 */
@Service
@Slf4j
public class AliYunApi extends ApacheHttpClient {
    public final static String USER_HOST = "patient-openapi.rplushealth.cn";
    public final static String DOCTOR_HOST = "doctor-openapi.rplushealth.cn";

    private String appKey;
    private String appSecret;

    public static final Long TIMEZONE_OFFSET = (long) (ZonedDateTime.now().getOffset().getTotalSeconds() / 60);

    public HttpClientBuilderParams getHttpParam() {
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(appKey);
        httpParam.setAppSecret(appSecret);
        httpParam.setScheme(Scheme.HTTP);
        return httpParam;
    }

    public HttpClientBuilderParams getHttpsParam() {
        HttpClientBuilderParams httpsParam = new HttpClientBuilderParams();
        httpsParam.setAppKey(appKey);
        httpsParam.setAppSecret(appSecret);
        httpsParam.setScheme(Scheme.HTTPS);
        return httpsParam;
    }

    @Value("${juse.shukang.appkey:203961652}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Value("${juse.shukang.appsecret:ae2N6SHWnUaoQcj4VqF4gbEaQfNCSQwA}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getResultString(ApiResponse response) {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if (response.getCode() != 200) {
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING));
        return result.toString();
    }

    public <T> ResponseBean<T> getResult(ApiRequest request, TypeReference<ResponseBean<T>> typeReference) {
        ApiResponse apiResponse = sendSyncRequest(request);
        return getResult(apiResponse, typeReference);

    }

    public <T> ResponseBean<T> getResult(ApiResponse apiResponse, TypeReference<ResponseBean<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        //忽略多余字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (apiResponse.getCode() != 200) {
            return ResponseBean.fail(apiResponse.getMessage());
        }
        try {
            return objectMapper.readValue(new String(apiResponse.getBody(), SdkConstant.CLOUDAPI_ENCODING), typeReference);
        } catch (JsonProcessingException e) {
            log.error("get result error :{}", e.getMessage());
            return ResponseBean.fail(getResultString(apiResponse));
        }
    }

    /**
     * 异步登录
     *
     * @param mobile     手机号
     * @param verifyCode 验证码
     * @param callback   回调
     * @param path       回调
     */
    public void signIn(String mobile, String verifyCode, String path, ApiCallback callback) {
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM, path);
        request.addParam("regionCode", "86", ParamPosition.QUERY, true);
        request.addParam("mobile", mobile, ParamPosition.QUERY, true);
        request.addParam("verifyCode", verifyCode, ParamPosition.QUERY, true);
        request.addParam("X-Ca-Stage", "PRE", ParamPosition.HEAD, false);
        sendAsyncRequest(request, callback);
    }


    /**
     * 异步
     * 刷新token
     *
     * @param refreshToken 入参
     * @param path         地址
     * @param callback     回调
     */
    public void refreshToken(String refreshToken, String path, ApiCallback callback) {
        ApiRequest request = new ApiRequest(HttpMethod.GET, path);
        request.addParam("refreshToken", refreshToken, ParamPosition.QUERY, true);
        sendAsyncRequest(request, callback);
    }

    /**
     * 异步
     * 获取手机验证码
     *
     * @param mobile   手机号
     * @param callback 回调
     * @param path     请求地址
     */
    public void getVerifyCode(String mobile, String path, ApiCallback callback) {
        ApiRequest request = new ApiRequest(HttpMethod.GET, path);
        request.addParam("regionCode", "86", ParamPosition.QUERY, true);
        request.addParam("mobile", mobile, ParamPosition.QUERY, true);
        sendAsyncRequest(request, callback);
    }

    /**
     * 同步
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @param path   请求地址
     * @return 验证码发送结果
     */
    public ApiResponse getVerifyCodeSyncMode(String mobile, String path) {
        ApiRequest request = new ApiRequest(HttpMethod.GET, path);
        request.addParam("regionCode", "86", ParamPosition.QUERY, true);
        request.addParam("mobile", mobile, ParamPosition.QUERY, true);
        return sendSyncRequest(request);
    }

    /**
     * 重试
     */
    @SneakyThrows
    public <T> void retry(ApiRequest apiRequest, Function<ResponseBean<T>, Object> function) {
        log.error("异步获取数据失败，尝试重试，path: {}", apiRequest.getPath());
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                //retryIf 重试条件
                .retryIfException()
                .retryIfRuntimeException()
                .retryIfExceptionOfType(Exception.class)
                .retryIfException(Predicates.equalTo(new Exception()))
                .retryIfResult(aBoolean -> Objects.equals(aBoolean, false))
                //等待策略：每次请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                //停止策略 : 尝试请求3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                //时间限制 : 某次请求不得超过2s , 类似: TimeLimiter timeLimiter = new SimpleTimeLimiter();
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
                .build();
        Callable<Boolean> callable = () -> {
            function.apply(getResult(apiRequest, new TypeReference<ResponseBean<T>>() {
            }));
            return true;
        };
        retryer.call(callable);
    }

}
