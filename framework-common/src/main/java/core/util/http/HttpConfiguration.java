package core.util.http;

/**
 * @Author tao.xiong
 * @Description http配置
 * @Date 2021/5/20 5:26 下午
 **/
public class HttpConfiguration {
    private long connectTimeOut;
    private long callTimeout;
    private long readTimeout;
    private boolean retryOnConnectionFailure;
    private String name;
    private String password;

    public long getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(long connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public long getCallTimeout() {
        return callTimeout;
    }

    public void setCallTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
