package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;

/**
 *@author Xue
 *@date 2018/7/1 17:01
 *@description  全局异常
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
