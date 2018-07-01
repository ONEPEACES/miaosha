package com.imooc.miaosha.redis;

public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getUserById = new UserKey("id");
    public static UserKey getUserByName= new UserKey("name");
}
