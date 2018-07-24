package com.imooc.miaosha.redis;

public class GoodsKey extends BasePrefix {

    public GoodsKey(int seconds, String prefix) {
        super(seconds, prefix);
    }

    public static GoodsKey goodsList = new GoodsKey(60,"gl");
    public static GoodsKey goodsDetail = new GoodsKey(60,"gd");
    public static GoodsKey MiaoshaGoodsStock = new GoodsKey(0,"gs");
}
