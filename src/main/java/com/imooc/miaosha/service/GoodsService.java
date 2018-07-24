package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoById(long goodsId);

    boolean updateGoodsWithGoodsId(MiaoshaGoods g);
}
