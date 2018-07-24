package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

import java.awt.image.BufferedImage;

public interface MiaoshaService {
    OrderInfo miaosha(MiaoshaUser user, GoodsVo goods);

    long getMiaoshaResult(MiaoshaUser id, long goodsId);

    String createMiaoshaPath(MiaoshaUser user, long goodsId);

    boolean checkMiaoshaPath(MiaoshaUser user, long goodsId, String path);

    BufferedImage createVerifyCode(MiaoshaUser user, long goodsId);

    boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode);
}
