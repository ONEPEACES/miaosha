package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.MiaoshaOrderMapper;
import com.imooc.miaosha.dao.OrderInfoMapper;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.OrderInfo;
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


    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
        MiaoshaOrder miaoshaOrder =  orderMapper.selectOrderWithUserIdAndGoodsId(userId,goodsId);
        return miaoshaOrder;
    }


    @Transactional
    @Override
    public OrderInfo placeAnOrder(Long userId, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setStatus((byte)0);
        orderInfo.setDeliverAddrId(0L);
        orderInfo.setOrderChannel((byte)1);
        int succ = orderInfoMapper.insertSelective(orderInfo);


        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(orderInfo.getGoodsId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(userId);
        orderMapper.insertSelective(miaoshaOrder);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderById(long orderId) {

        return orderInfoMapper.selectByPrimaryKey(orderId);
    }
}
