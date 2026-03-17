package com.xt.framework.mq.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xt.framework.mq.core.KafkaConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.time.Duration;
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
        // 【Fan-Out关键】开启精确一次语义，保证写入多个 Topic 的原子性
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        // 可选：如果需要处理大消息，建议显式设置缓存和缓冲区大小
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, MAX_MESSAGE_SIZE);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        return new KafkaStreamsConfiguration(props);
    }

    /**
     *
     * @param streamsBuilder 实时 ETL (清洗与转换)
     * @return 过滤脏数据、格式转换、字段脱敏，将原始日志转为标准数据
     */
    @Bean
    public KStream<String, String> EStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> rawStream = streamsBuilder.stream("raw-logs");
        ObjectMapper mapper = new ObjectMapper();
        // 2. 清洗与转换逻辑
        KStream<String, String> cleanStream = rawStream
                // 过滤：只保留能解析为 JSON 且 level 不为 "DEBUG" 的日志
                .filter((key, value) -> {
                    try {
                        JsonNode json = mapper.readTree(value);
                        String level = json.has("level") ? json.get("level").asText() : "";
                        return !"DEBUG".equals(level);
                    } catch (Exception e) {
                        return false; // 解析失败的丢弃
                    }
                })
                // 映射：只保留 timestamp, message, level 三个字段，重新序列化为 JSON
                .mapValues(value -> {
                    try {
                        JsonNode json = mapper.readTree(value);
                        ObjectNode out = mapper.createObjectNode();
                        out
                                .put("ts", json.has("timestamp") ? json.get("timestamp").asLong() : System.currentTimeMillis())
                                .put("msg", json.has("message") ? json.get("message").asText() : "")
                                .put("lvl", json.has("level") ? json.get("level").asText() : "INFO");
                        return mapper.writeValueAsString(out);
                    } catch (Exception e) {
                        return value;
                    }
                });

        // 3. 写入目标 Topic
        cleanStream.to("clean-logs", Produced.with(Serdes.String(), Serdes.String()));
        return cleanStream;
    }

    /**
     * 技术点：groupByKey + windowedBy (滚动窗口、跳跃窗口、会话窗口) + aggregate/count
     * @param streamsBuilder 实时聚合统计 (Real-time Aggregation)
     * @return 计算实时 GMV、每分钟 PV/UV、设备平均温度
     */
    @Bean
    public KStream<String, String> AStream(StreamsBuilder streamsBuilder) {
        //创建KStream对象，同时指定从那个topic中接收消息
        log.info("正在构建 Kafka Streams 拓扑...");
        KStream<String, String> stream = streamsBuilder.stream(KafkaConstants.STREAM_INPUT_TOPIC);
        //滚动窗口 (Tumbling Window)：固定大小、不重叠、无间隙（每1分钟一个桶）
        TimeWindows timeWindows = TimeWindows.of(Duration.ofMinutes(1));
        //跳跃窗口 (Hopping Window)：固定大小、可重叠、有步长。(过去5分钟，每1分钟更新一次结果)
        TimeWindows hoppingWindows = TimeWindows.of(Duration.ofMinutes(5)).advanceBy(Duration.ofMinutes(1));
        //滑动窗口 (Sliding Window)：基于事件时间间隔，动态重叠（常用于 Join）
        /*SlidingWindows slidingWindows = SlidingWindows.ofTimeDifferenceAndGrace(
                Duration.ofMinutes(10),
                Duration.ofMinutes(2));*/
        // 会话窗口 (Session Window)：基于活动间隙（Inactivity Gap），动态大小（如果用户在 30 分钟内没有新事件，会话结束并输出结果）
        SessionWindows sessionWindows = SessionWindows.with(Duration.ofMinutes(30));
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
                .windowedBy(sessionWindows)
                .count(Materialized.as("word-counts-store"))
                .toStream()
                .mapValues(String::valueOf)
                .peek((k, v) -> System.out.println("Word: " + k + ", Count: " + v)) // 打印日志观察
                .to(KafkaConstants.STREAM_OUTPUT_TOPIC, Produced.with(WindowedSerdes.sessionWindowedSerdeFrom(String.class), Serdes.String()));
        return stream;
    }

    /**
     * 技术点：状态存储 (State Store)、窗口操作、分支流 (branch)
     * @param streamsBuilder 实时风控与异常检测
     * @return 检测短时间内的频繁交易、登录失败次数突增（配合状态存储）
     */
    @Bean
    public KStream<String, String> TStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> transactions = streamsBuilder.stream("transactions-input");
        ObjectMapper mapper = new ObjectMapper();
        // 定义分支逻辑
        Predicate<String, String> isHighRisk = (key, value) -> {
            try {
                JsonNode json = mapper.readTree(value);
                double amount = json.get("amount").asDouble();
                return amount > 10000.0;
            } catch (Exception e) {
                return false; // 解析失败视为非高风险或单独处理
            }
        };
        // branch 返回一个数组，索引 0 对应 isHighRisk 为 true 的流，索引 1 为 false
        KStream<String, String>[] branches = transactions.branch(isHighRisk);

        KStream<String, String> highRiskStream = branches[0];
        KStream<String, String> normalStream = branches[1];

        // 添加标记并输出
        highRiskStream
                .mapValues(v -> v + ",\"status\":\"FLAGGED\"")
                .to("transactions-high-risk");

        normalStream
                .to("transactions-normal");
        return normalStream;
    }

    /**
     * 技术点：Stream-Table Join
     * @param streamsBuilder  数据增强 (Stream-Table Join)
     * @return  用户行为分析与实时推荐，将实时点击流与用户信息表关联
     */
    @Bean
    public KStream<String, String> SStream(StreamsBuilder streamsBuilder) {

        // 1. 定义订单流
        KStream<String, String> orders = streamsBuilder.stream("orders-topic");
        // 2. 定义用户表 (KTable 会自动维护每个 UserId 的最新值)
        KTable<String, String> users = streamsBuilder.table("users-topic");

        // 3. 执行 Join
        // 注意：Stream-Table Join 不需要窗口，它是基于当前状态的即时查找
        KStream<String, String> enrichedOrders = orders.join(
                users,
                (orderValue, userValue) -> {
                    // 合并逻辑：将订单信息和用户等级合并
                    return orderValue + ", \"userLevel\": \"" + (userValue != null ? userValue : "UNKNOWN") + "\"";
                }
        );

        enrichedOrders.to("orders-enriched-output");
        return enrichedOrders;
    }

    /**
     *
     * @param streamsBuilder 多路分发 (Fan-out)
     * @return 将一份数据处理后同时写入多个下游系统（如数仓、告警、搜索索引）
     */
    @Bean
    public KStream<String, String> FStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> orders = streamsBuilder.stream("orders-input");

        // 简单处理
        KStream<String, String> processed = orders.mapValues(v -> v + ",\"processed\":true");

        // 一路多发：同一个 Stream 对象调用多次 .to()
        processed.to("data-warehouse-topic");
        processed.to("realtime-dashboard-topic");
        processed.to("audit-log-topic");
        return orders;
    }
}
