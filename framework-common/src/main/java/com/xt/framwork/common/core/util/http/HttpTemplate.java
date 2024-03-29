package com.xt.framwork.common.core.util.http;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xt.framwork.common.core.util.JsonUtils;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @Author tao.xiong
 * @Description http封装
 * @Date 2021/5/12 8:23 下午
 **/
public class HttpTemplate extends HttpAccessor implements HttpOperations {
    private static final Logger log = LoggerFactory.getLogger(HttpTemplate.class);
    private final MediaType json = MediaType.parse("application/json;charset=utf-8");

    public HttpTemplate() {
        this.setDefaultConnect();
    }

    public HttpTemplate(ObjectNode config) {
        this.setConnect(config);
    }

    @Override
    public HttpResponse get(String url) {
        try {
            Request request = new Request.Builder().url(url).get().build();
            return new HttpResponse(this.getOkHttpClient().newCall(request).execute());
        } catch (IOException e) {
            log.error("http get error:{},{}", url, e.getMessage());
            return HttpResponse.exceptionExecute();
        }
    }

    @Override
    public HttpResponse post(String url, String json) {
        try {
            RequestBody requestBody = RequestBody.create(this.json, json);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            return new HttpResponse(this.getOkHttpClient().newCall(request).execute());
        } catch (IOException e) {
            log.error("http post error:{},{},{}", url, json, e.getMessage());
            return HttpResponse.exceptionExecute();
        }
    }

    @Override
    public HttpResponse post(String url, Map<String, String> headMap) {
        try {
            Headers headers = Headers.of(headMap);
            Request request = new Request.Builder().url(url).headers(headers).build();
            return new HttpResponse(this.getOkHttpClient().newCall(request).execute());
        } catch (IOException e) {
            log.error("http post error:{},{},{}", url, JsonUtils.encode(headMap), e.getMessage());
            return HttpResponse.exceptionExecute();
        }
    }

    @Override
    public HttpResponse post(String url, String json, Map<String, String> headMap) {
        try {
            RequestBody requestBody = RequestBody.create(this.json, json);
            Headers headers = Headers.of(headMap);
            Request request = new Request.Builder().url(url).post(requestBody).headers(headers).build();
            return new HttpResponse(this.getOkHttpClient().newCall(request).execute());
        } catch (IOException e) {
            log.error("http post error:{},{},{},{}", url, json, JsonUtils.encode(headMap), e.getMessage());
            return HttpResponse.exceptionExecute();
        }
    }
}
