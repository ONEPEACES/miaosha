package com.imooc.miaosha.controller;

import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);

        //检查库存
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        Integer stockCount = goods.getStockCount();

        if (stockCount <= 0) {
//            model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
//            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //1 减库存
        //2 下订单
        //3 写入秒杀订单
        //1 2 3 要做成原子操作
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        model.addAttribute("orderInfo",orderInfo);
//        model.addAttribute("goods",goods);
        return Result.success(orderInfo);
    }

}
