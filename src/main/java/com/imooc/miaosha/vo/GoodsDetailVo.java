package com.imooc.miaosha.vo;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaUser;

public class GoodsDetailVo {
    private int miaoshaStatus;
    private int remainSeconds;
    private Goods goods;
    private MiaoshaUser miaoshaUser;

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }
}
