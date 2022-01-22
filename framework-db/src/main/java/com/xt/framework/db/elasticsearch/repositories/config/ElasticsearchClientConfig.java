package com.xt.framework.db.elasticsearch.repositories.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author tao.xiong
 * @Description es 配置
 * @Date 2022/1/18 17:18
 */
@Configuration
@EnableElasticsearchRepositories(basePackages
        = "com.xt.framework.db.elasticsearch.repositories")
@ComponentScan(basePackages = {"com.xt.framework.db.elasticsearch"})
public class ElasticsearchClientConfig extends
        AbstractElasticsearchConfiguration {
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration =
                ClientConfiguration
                        .builder()
                        .connectedTo("192.168.1.5:9200")
                        .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
