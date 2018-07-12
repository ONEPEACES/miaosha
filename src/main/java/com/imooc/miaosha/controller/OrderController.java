package com.imooc.miaosha.controller;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import com.imooc.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> getOrderDetail(MiaoshaUser user,
                                            @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long id = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoById(id);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoods(goodsVo);
        orderDetailVo.setOrder(orderInfo);
        return Result.success(orderDetailVo);
    }


}
