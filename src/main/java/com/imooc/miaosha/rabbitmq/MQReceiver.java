package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    /**
     * 消费
     * @param msg
     */
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String msg){
        log.info("receive message:"+msg);
        MiaoshaMsg miaoshaMsg = RedisService.stringToBean(msg, MiaoshaMsg.class);
        MiaoshaUser user = miaoshaMsg.getUser();
        long goodsId = miaoshaMsg.getGoodsId();
        // 判断库存是否为空
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            return;
        }
        //判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user, goodsId);
        if (miaoshaOrder != null) {
            return;
        }
        // 减库存
        // 生成订单
        // 写入秒杀订单
        miaoshaService.miaosha(user,goodsVo);
    }



}
