package com.xt.framework.db.elasticsearch.config;

import io.micrometer.core.lang.NonNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author tao.xiong
 * @Description es 配置
 * @Date 2022/1/18 17:18
 */
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@EnableElasticsearchRepositories(basePackages
        = "com.xt.*.db.elasticsearch.repositories")
@ComponentScan(basePackages = {"com.xt.framework.db.elasticsearch"})
@Data
public class ElasticSearchClientConfig extends
        AbstractElasticsearchConfiguration {
    private static String host;
    private static Integer port;

    @Override
    @Bean
    @NonNull
    public RestHighLevelClient elasticsearchClient() {
        return buildClient();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

    public static RestHighLevelClient buildClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port));
        return new RestHighLevelClient(builder);
    }
}
