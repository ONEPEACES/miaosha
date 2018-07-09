package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.MiaoshaOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MiaoshaOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaOrder record);

    int insertSelective(MiaoshaOrder record);

    MiaoshaOrder selectByPrimaryKey(Long id);

    MiaoshaOrder selectOrderWithUserIdAndGoodsId(@Param("userId")Long userId,@Param("goodsId") long goodsId);

    int updateByPrimaryKeySelective(MiaoshaOrder record);

    int updateByPrimaryKey(MiaoshaOrder record);
}