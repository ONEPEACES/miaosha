package com.imooc.miaosha.controller;

import com.imooc.miaosha.access.AccessLimit;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMsg;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> over;


    /**
     * 1.系统初始化后执行将商品数量加载到缓存
     *
     * @param
     * @return
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null) {
            return;
        }
        over = new HashMap<>();
        //商品库存加载到redis里面
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.MiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            over.put(goodsVo.getId(), false);
        }
    }

    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId") long goodsId,
                                     @PathVariable("path") String path) {
        if (user == null) {
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        //验证用户秒杀接口是否正常
        boolean check = miaoshaService.checkMiaoshaPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        // 2.减缓存库存，如果库存减至小于0说明redis预存库存已被秒杀完
        // 内存标记优化
        Boolean over = this.over.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        Long succ = redisService.decr(GoodsKey.MiaoshaGoodsStock, "" + goodsId);
        if (succ < 0) {
            this.over.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 3.判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user, goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        // 还未执行一次秒杀，则将秒杀入队
        MiaoshaMsg mm = new MiaoshaMsg(user, goodsId);
        mqSender.sendToMsg(mm);
        // 0：代表排队中
        return Result.success(0);


        /*
        //检查库存
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        Integer stockCount = goods.getStockCount();

        if (stockCount <= 0) {
//            model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        // 缓存优化
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user, goodsId);
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
        */
//        return Result.success(new OrderInfo());
    }

    /**
     * 秒杀成功：返回orderId
     * 秒杀失败：-1
     * 排队中：0
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user, goodsId);
        return Result.success(result);
    }


    /**
     * 仅仅加入一个隐藏接口的设置并不能防止刷接口
     *
     * @param
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> miaoshaPath(Model model, MiaoshaUser user,
                                      @RequestParam("goodsId") long goodsId,
                                      //超过限流验证
                                      @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode) {
        if (user == null) {
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> miaoshaVerifyCode(Model model, MiaoshaUser user,
                                            HttpServletResponse response,
                                            @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
