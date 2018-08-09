package com.imooc.miaosha.rabbitmq;

import java.lang.String;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String TOPIC_QUEUE = "topic.queue";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String HEADERS_EXCHANGE = "headers.exchange";

    /**
     * Direct模式 点对点，直接发送无过滤方式
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

    @Bean
    public Queue topicQueue() {
        return new Queue(TOPIC_QUEUE, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        // exchange 路由概念
        // 通过一定的exchange规则将数据路由到接收方
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * 对topic队列进行过滤 #代表所有的都接收
     *
     * @return
     */
    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("topic.#");
    }

    /**
     * 广播
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(topicQueue()).to(fanoutExchange());
    }

    /**
     * headersExchange 模式 发送方必须带有map结构数据
     * @return
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Binding headersBinding(){
        Map<String,Object> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2");
        return BindingBuilder.bind(topicQueue()).to(headersExchange()).whereAll(map).match();
    }

}
