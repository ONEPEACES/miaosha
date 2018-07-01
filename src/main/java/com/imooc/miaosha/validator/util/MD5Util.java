package com.imooc.miaosha.validator.util;


import org.apache.commons.codec.digest.DigestUtils;

public final class MD5Util {

    private static final String salt = "1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     * 客户端用于传输的md5加密后的密码，用的固定salt
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(4) + salt.charAt(6);
        return md5(str);
    }

    /**
     * 服务端用于存储的md5二次加密的密码，随机salt并且存储在数据库中
     * @param FormPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String FormPass,String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + FormPass + salt.charAt(4) + salt.charAt(6);
        return md5(str);
    }

    /**
     * 返回服务端二次md5密码
     * @param inputPass
     * @param saltDB
     * @return
     */
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass, saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDbPass("123456","12345678"));
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"),"12345678"));;
    }


}
