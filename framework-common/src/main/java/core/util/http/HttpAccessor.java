package core.util.http;

import com.fasterxml.jackson.databind.node.ObjectNode;
import core.util.JsonUtils;
import okhttp3.Authenticator;
import okhttp3.ConnectionPool;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @Author tao.xiong
 * @Description
 * @Date 2021/5/12 8:26 下午
 **/
public class HttpAccessor {
    protected ConnectionPool connectionPool = new ConnectionPool(5, 10, TimeUnit.MINUTES);
    private HttpConfiguration httpConfiguration;

    private OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public HttpConfiguration getDefaultHttpConfiguration() {
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setConnectTimeOut(30);
        httpConfiguration.setCallTimeout(30);
        httpConfiguration.setReadTimeout(120);
        httpConfiguration.setRetryOnConnectionFailure(true);
        return httpConfiguration;
    }

    public void setConnect(ObjectNode config) {
        this.httpConfiguration = JsonUtils.readUpdate(config, getDefaultHttpConfiguration());
        setConnect();

    }

    public void setDefaultConnect() {
        this.httpConfiguration = getDefaultHttpConfiguration();
        setConnect();
    }

    protected void setConnect() {
        Authenticator authenticator = (route, response) -> {
            String credential = Credentials.basic(this.httpConfiguration.getName(), this.httpConfiguration.getPassword());
            return response.request().newBuilder().header("Authorization", credential).build();
        };
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(this.httpConfiguration.getConnectTimeOut(), TimeUnit.SECONDS)
                .callTimeout(this.httpConfiguration.getCallTimeout(), TimeUnit.SECONDS)
                .readTimeout(this.httpConfiguration.getReadTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(this.httpConfiguration.isRetryOnConnectionFailure())
                .authenticator(authenticator)
                .connectionPool(connectionPool)
                .build();
    }
}
