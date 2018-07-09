package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoById(long goodsId);

    void updateGoodsWithGoodsId(MiaoshaGoods g);
}
