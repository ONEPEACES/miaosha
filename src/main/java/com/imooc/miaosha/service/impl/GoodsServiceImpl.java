package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.GoodsMapper;
import com.imooc.miaosha.dao.MiaoshaGoodsMapper;
import com.imooc.miaosha.entity.MiaoshaGoods;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MiaoshaGoodsMapper miaoshaGoodsMapper;


    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    /**
     * 查询单个商品
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo getGoodsVoById(long goodsId) {
        return goodsMapper.selectGoodsVoWithGoodsId(goodsId);
    }


    @Override
    public boolean updateGoodsWithGoodsId(MiaoshaGoods g) {
        int result = miaoshaGoodsMapper.updateMiaoshaGoodsWithGoods(g);
        if(result != 0){
            return true;
        }
        return false;
    }
}
