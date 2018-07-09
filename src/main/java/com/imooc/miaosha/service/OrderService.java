package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

public interface OrderService {

    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, long goodsId);

    OrderInfo placeAnOrder(Long id, GoodsVo goods);
}
