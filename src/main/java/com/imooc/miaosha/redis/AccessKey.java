package com.imooc.miaosha.redis;

public class AccessKey extends BasePrefix {
    public AccessKey(int seconds, String prefix) {
        super(seconds, prefix);
    }

    public AccessKey(String prefix) {
        super(prefix);
    }

    public static AccessKey generateKey(int seconds){
        return new AccessKey(seconds,"ak");
    }
}
