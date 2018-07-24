package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.entity.MiaoshaUser;

public class MiaoshaMsg {
    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaMsg(MiaoshaUser user, long goodsId) {
        this.user = user;
        this.goodsId = goodsId;
    }

    public MiaoshaMsg() {
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
