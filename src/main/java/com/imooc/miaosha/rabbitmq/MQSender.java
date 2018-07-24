package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    /**
     * MQ发送方通过AmqpTemplate进行发送
     */
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendToMsg(MiaoshaMsg mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send msgs:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
