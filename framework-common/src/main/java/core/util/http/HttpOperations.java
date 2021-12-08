package core.util.http;

import java.util.Map;

public interface HttpOperations {
    /**
     * 执行
     * @param url 链接
     * @return 请求结果
     */
    HttpResponse execute(String url);

    /**
     * 执行
     * @param url 链接
     * @param json 请求体
     * @return 请求结果
     */
    HttpResponse execute(String url,String json);

    /**
     * 执行
     * @param url 链接
     * @param headMap 请求头
     * @return 请求结果
     */
    HttpResponse execute(String url,Map<String,String> headMap);

    /**
     * 执行
     * @param url 链接
     * @param json 请求体
     * @param headMap 请求头
     * @return 请求结果
     */
    HttpResponse execute(String url, String json, Map<String,String> headMap);

}
