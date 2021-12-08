package core.aliyun;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.rholder.retry.*;
import core.bean.ResponseBean;
import core.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author tao.xiong
 * @Description 阿里云api初始化
 * @Date 2021/8/7 14:57
 */
@Service
@Slf4j
public class AliYunApi extends ApacheHttpClient {
    public final static String DOCTOR_HOST = "doctor-openapi.rplushealth.cn";

    public String profile;

    private String appKey;
    private String appSecret;

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

    @Value("${juse.shukang.profile:RELEASE}")
    public void setProfile(String profile) {
        this.profile = profile;
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
        log.info("同步获取数据 返回 url:{} request :{} response :{} ",
                request.getUrl(), request.getBodyStr(), getResultString(apiResponse));
        return getResult(apiResponse, typeReference);

    }

    public <T> ResponseBean<T> getResult(ApiResponse apiResponse, TypeReference<ResponseBean<T>> typeReference) {
        //忽略多余字段
        if (apiResponse.getCode() != 200) {
            return ResponseBean.fail(apiResponse.getCode(),apiResponse.getMessage());
        }
        try {
            return JsonUtils.parseJson(new String(apiResponse.getBody(), SdkConstant.CLOUDAPI_ENCODING), typeReference);
        } catch (Exception e) {
            log.error("get result error :{}", e.getMessage());
            return ResponseBean.fail(getResultString(apiResponse));
        }
    }

    /**
     * 异步重试请求
     *
     * @param apiRequest 请求
     * @param function   数据处理
     */
    protected void sendAsyncRetryRequest(ApiRequest apiRequest, Function<ResponseBean<Object>, Object> function) {
        ApiCallback apiCallback = new ApiCallback() {
            @Override
            public void onFailure(ApiRequest apiRequest, Exception e) {
                log.error("异步获取数据 失败 重试 url:{} request :{} message :{} ",
                        apiRequest.getPath(), apiRequest.getBodyStr(), e.getMessage());
                retry(apiRequest, function);
            }

            @Override
            public void onResponse(ApiRequest apiRequest, ApiResponse apiResponse) {
                log.info("异步获取数据返回 url:{} , response :{} ",
                        apiRequest.getPath(), getResultString(apiResponse));
                function.apply(getResult(apiResponse, new TypeReference<ResponseBean<Object>>() {
                }));
            }
        };
        sendAsyncRequest(apiRequest, apiCallback);
    }

    /**
     * 重试
     */
    @SneakyThrows
    public void retry(ApiRequest apiRequest, Function<ResponseBean<Object>, Object> function) {
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                //retryIf 重试条件
                .retryIfException()
                .retryIfRuntimeException()
                .retryIfExceptionOfType(Exception.class)
                .retryIfException(throwable -> Objects.equals(throwable, new Exception()))
                .retryIfResult(aBoolean -> Objects.equals(aBoolean, false))
                //等待策略：每次请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                //停止策略 : 尝试请求3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                //时间限制 : 某次请求不得超过2s , 类似: TimeLimiter timeLimiter = new SimpleTimeLimiter();
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
                .build();
        Callable<Boolean> callable = () -> {
            function.apply(getResult(apiRequest, new TypeReference<ResponseBean<Object>>() {
            }));
            return true;
        };
        retryer.call(callable);
    }

}
