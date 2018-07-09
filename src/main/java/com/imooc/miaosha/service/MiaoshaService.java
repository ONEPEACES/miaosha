package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

public interface MiaoshaService {
    OrderInfo miaosha(MiaoshaUser user, GoodsVo goods);
}
