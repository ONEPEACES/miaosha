package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    GoodsVo selectGoodsVoWithGoodsId(@Param("goodsId")long goodsId);

    List<GoodsVo> listGoodsVo();

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

}