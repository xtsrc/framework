package com.xt.framework.mq.core.config;

import com.xt.framework.mq.core.KafkaConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Configuration
@Slf4j
@EnableKafkaStreams
@ConfigurationProperties(prefix = "kafka")
public class KafkaStreamConfig {
    private static final int MAX_MESSAGE_SIZE = 16 * 1024 * 1024;

    private String hosts;
    private String group;

    /**
     * 创建并配置 Kafka Streams 的默认配置 Bean
     * <p>
     * 此 Bean 用于初始化 KafkaStreams 应用实例，必须命名为
     * {@link KafkaStreamsDefaultConfiguration#DEFAULT_STREAMS_CONFIG_BEAN_NAME}
     * 才能被 Spring Kafka 正确识别和使用。
     *
     * @return 额外的 Java 配置 - 可以覆盖 YML 中的默认值
     */
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration defaultKafkaStreamsConfig() {

        Map<String, Object> props = new HashMap<>();

        // 设置 Kafka 集群的连接地址（Broker 列表）
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);

        // 设置应用 ID（Application ID），用于标识一个 Kafka Streams 应用
        // 同一个应用 ID 的实例属于同一个消费者组，用于容错和状态恢复
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, this.getGroup() + "_stream_aid");

        // 设置客户端 ID（Client ID），用于标识该 Streams 实例，在 Kafka 服务端日志和监控中可见
        props.put(StreamsConfig.CLIENT_ID_CONFIG, this.getGroup() + "_stream_cid");

        // 设置生产者/处理器重试次数（部分操作如生产结果消息时会重试）
        // 注意：Streams 内部有更复杂的容错机制，此配置主要用于生产阶段的重试
        props.put(StreamsConfig.RETRIES_CONFIG, 10);

        // 设置默认的 key 序列化/反序列化器（Serde）
        // 这里使用 StringSerde，表示 key 为字符串类型
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 设置默认的 value 序列化/反序列化器（Serde）
        // 这里使用 LongSerde，表示 value 为Long类型
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 可选：如果需要处理大消息，建议显式设置缓存和缓冲区大小
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, MAX_MESSAGE_SIZE);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        //创建KStream对象，同时指定从那个topic中接收消息
        log.info("正在构建 Kafka Streams 拓扑...");
        KStream<String, String> stream = streamsBuilder.stream(KafkaConstants.STREAM_INPUT_TOPIC);
        // 处理逻辑：
        // 1. mapValues: 将句子转为小写
        // 2. flatMapValues: 按空格拆分句子为单词数组，并展开为多条记录
        // 3. selectKey: 将单词本身作为 Key (以便后续按单词分组)
        // 4. groupByKey: 按 Key (单词) 分组
        // 5. count: 计数，结果是一个 KTable<String, Long>
        stream.mapValues(s -> s.toLowerCase())
                .flatMapValues(value -> Arrays.asList(value.split("\\W+"))) // 非单词字符分割
                .selectKey((key, word) -> word)
                .groupByKey()
                .count(Materialized.as("word-counts-store"))
                .toStream()
                .mapValues(String::valueOf)
                .peek((k, v) -> System.out.println("Word: " + k + ", Count: " + v)) // 打印日志观察
                .to(KafkaConstants.STREAM_OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
        return stream;
    }
}
