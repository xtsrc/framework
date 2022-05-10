package com.xt.framework.mq.core.config;

import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tao.xiong
 * @Description pulsar配置
 * @Date 2021/8/30 15:30
 */
@Configuration
public class PulsarConfig {
    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory()
                .addProducer("bootTopic", PulsarMessage.class)
                .addProducer("stringTopic", String.class);
    }
}
