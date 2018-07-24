package com.imooc.miaosha.redis;

public class MiaoshaKey extends BasePrefix {
    public MiaoshaKey(String prefix) {
        super(0,prefix);
    }
    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
    public static MiaoshaKey miaoshaPath = new MiaoshaKey("mp");
    public static MiaoshaKey miaoshaVerifyCode = new MiaoshaKey("mvc");
}
