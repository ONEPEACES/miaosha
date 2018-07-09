package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    String login(HttpServletResponse response, LoginVo loginVo);

    MiaoshaUser getByToken(HttpServletResponse response, String token);
}
