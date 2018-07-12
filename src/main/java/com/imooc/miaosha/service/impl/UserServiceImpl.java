package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.constant.GlobalConst;
import com.imooc.miaosha.dao.MiaoshaUserMapper;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MiaoshaUserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(long id) {
        //从缓存中取用户
        MiaoshaUser user = redisService.get(UserKey.getUserById, "" + id, MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        //设置缓存对象
        user = userMapper.selectByPrimaryKey(id);
        redisService.set(UserKey.getUserById, "" + id, user);
        return user;
    }

    public boolean updatePassword(String token, String formPass, long id) {
        //根据用户手机号获取用户信息
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        user.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userMapper.updateByPrimaryKeySelective(user);
        //更新缓存
        redisService.delete(UserKey.getUserById, "" + id);
        //修改缓存中token
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = userMapper.selectByMobile(mobile);
        if (null == user) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(GlobalConst.COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
