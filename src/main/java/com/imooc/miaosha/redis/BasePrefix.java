package com.imooc.miaosha.redis;

/**
 *@author Xue
 *@date 2018/7/1 17:20
 *@description  给子类定义一个模板，子类调用父类默认方法获得对应的属性
 */
public abstract class BasePrefix  implements KeyPrefix{

    private int seconds;
    private String prefix;

    /**
     * 0代表永不过期
     * @param prefix
     */
    public BasePrefix(String prefix) {
        this(0,prefix);
    }

    public BasePrefix(int seconds, String prefix){
        this.seconds = seconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return seconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
