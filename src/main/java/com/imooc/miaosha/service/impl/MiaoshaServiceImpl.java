package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.entity.MiaoshaGoods;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaServiceImpl implements MiaoshaService {

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private OrderService orderService;


    @Transactional
    @Override
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //1减库存
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        goodsService.updateGoodsWithGoodsId(g);
        //2下订单
        //3写入秒杀订单
        return orderService.placeAnOrder(user.getId(),goods);
    }
}
