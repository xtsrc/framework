package core.util.http;

import core.util.JsonUtils;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author tao.xiong
 * @Description
 * @Date 2021/5/12 8:41 下午
 **/
public class HttpResponse {
    private Response response;

    public HttpResponse(Response response) {
        this.response = response;
    }

    public HttpResponse() {
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public String toString() {
        try {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            return null;
        } finally {
            Objects.requireNonNull(response.body()).close();
        }
    }

    public <T> T toClass(Class<T> tClass) {
        try {
            return JsonUtils.readUpdate(JsonUtils.transToNode(Objects.requireNonNull(response.body()).string()), tClass.newInstance());
        } catch (Exception e) {
            return null;
        } finally {
            Objects.requireNonNull(response.body()).close();
        }
    }

    public static HttpResponse exceptionExecute() {
        return new HttpResponse();
    }
}
