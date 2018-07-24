package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

public interface OrderService {

    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(MiaoshaUser user, long goodsId);

    OrderInfo placeAnOrder(MiaoshaUser user, GoodsVo goods);

    OrderInfo getOrderById(long orderId);
}
