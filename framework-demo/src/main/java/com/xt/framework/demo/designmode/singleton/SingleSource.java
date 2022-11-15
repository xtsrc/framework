package com.xt.framework.demo.designmode.singleton;

/**
 * @author tao.xiong
 * @Description 单例
 * @Date 2021/9/10 22:55
 */
public enum SingleSource implements SingleConnectSource {
    /**
     * 枚举实现单例
     */
    INSTANCE {
        @Override
        public String download(String token) {
            return REST_TEMPLATE.postForObject(String.format(URL, token), null, String.class);
        }

        @Override
        public void upload(String token, String data) {
            REST_TEMPLATE.postForObject(String.format(URL, token), data, String.class);
        }
    };

    public static SingleSource getInstance() {
        return INSTANCE;
    }
}
