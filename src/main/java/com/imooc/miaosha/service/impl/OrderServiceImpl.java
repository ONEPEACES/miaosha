package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.MiaoshaOrderMapper;
import com.imooc.miaosha.dao.OrderInfoMapper;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MiaoshaOrderMapper orderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(MiaoshaUser user, long goodsId) {

//        return orderMapper.selectOrderWithUserIdAndGoodsId(user.getId(),goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + "_" + goodsId, MiaoshaOrder.class);
    }


    @Transactional
    @Override
    public OrderInfo placeAnOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getId());
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setStatus((byte) 0);
        orderInfo.setDeliverAddrId(0L);
        orderInfo.setOrderChannel((byte) 1);
        int succ = orderInfoMapper.insertSelective(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(orderInfo.getGoodsId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderMapper.insertSelective(miaoshaOrder);
        //缓存订单
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + "_" + goods.getId(), miaoshaOrder);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderById(long orderId) {

        return orderInfoMapper.selectByPrimaryKey(orderId);
    }
}
