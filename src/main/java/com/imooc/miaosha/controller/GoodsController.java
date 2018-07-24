package com.imooc.miaosha.controller;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThymeleafViewResolver resolver;

    /**
     * produces 返回类型
     * 页面缓存
     * jmeter：1000*10 throughput 245qps未修改
     */
    @RequestMapping(value = "/to_list")
//    @ResponseBody
    public String toList(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response) {
        //从缓存中取
        //缓存60秒，用户只能看到60秒之前已经修改好的页面，这60秒内页面不会改变
//        String html = redisService.get(GoodsKey.goodsList, "", String.class);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }

        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        return "goods_list";

//        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
//                request.getLocale(), model.asMap(), applicationContext);
//        html = resolver.getTemplateEngine().process("goods_list", springWebContext);
//        redisService.set(GoodsKey.goodsList, "", html);
//        return html;
    }

    /**
     * 返回的是一个经过视图解析器解析后的页面
     * @param
     * @return
     */
    @RequestMapping(value = "/to_list1", produces = "text/html")
    @ResponseBody
    public String toList1(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response) {
        //从缓存中取
        //缓存60秒，用户只能看到60秒之前已经修改好的页面，这60秒内页面不会改变
        String html = redisService.get(GoodsKey.goodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
//        return "goods_list";

        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = resolver.getTemplateEngine().process("goods_list", springWebContext);
        redisService.set(GoodsKey.goodsList, "", html);
        return html;
    }


    /**
     * 商品详情
     * 页面缓存，最先从缓存中取，取不到就进行数据库的查询，然后添加到数据库
     * 但是缓存最好设置较短的时间，用户在使用缓存的这段时间，缓存的内容并不会因为其他用户修改了部分信息而修改
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toList(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId
            , HttpServletRequest request, HttpServletResponse response) {
        String html = redisService.get(GoodsKey.goodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //snowflake 自增id算法
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now > end) {
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else if (now < start) {
            //秒杀未开始
            miaoshaStatus = 0;
            remainSeconds = (int) ((start - now) / 1000);
        } else {
            //秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("user", user);
        model.addAttribute("goods", goods);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";

        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = resolver.getTemplateEngine().process("goods_detail", springWebContext);
        redisService.set(GoodsKey.goodsDetail, "" + goodsId, html);
        return html;
    }


    /**
     * 前后端分离
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/to_detail1/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toList2(MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        //snowflake 自增id算法
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now > end) {
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else if (now < start) {
            //秒杀未开始
            miaoshaStatus = 0;
            remainSeconds = (int) ((start - now) / 1000);
        } else {
            //秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setMiaoshaUser(user);
//        return "goods_detail";

        return Result.success(goodsDetailVo);
    }


}
