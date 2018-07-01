package com.imooc.miaosha.redis;

/**
 *@author Xue
 *@date 2018/6/30 22:54
 *@description  对于不同类型的key 的前缀提供了一个接口
 */
public interface KeyPrefix {
    /**
     *
     * @return 设置key的缓存时间
     */
    int expireSeconds();
    /**
     *
     * @return redis key 的前缀
     */
    String getPrefix();
}
