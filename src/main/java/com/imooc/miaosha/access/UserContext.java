package com.imooc.miaosha.access;

import com.imooc.miaosha.entity.MiaoshaUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 *@author Xue
 *@date 2018/7/24 14:42
 *@description  线程私有
 */
public class UserContext {
    private static ThreadLocal<MiaoshaUser> userThreadLocal = new ThreadLocal<>();

    public static MiaoshaUser getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(MiaoshaUser user) {
        userThreadLocal.set(user);
    }
}
