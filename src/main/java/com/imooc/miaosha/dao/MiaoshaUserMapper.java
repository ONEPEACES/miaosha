package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MiaoshaUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaUser record);

    int insertSelective(MiaoshaUser record);

    MiaoshaUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaUser record);

    int updateByPrimaryKey(MiaoshaUser record);

    MiaoshaUser selectByMobile(String mobile);
}