package com.imooc.miaosha.entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo {
    private Long id;

    private Long userId;

    private Long goodsId;

    private Long deliverAddrId;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsPrice;

    //1pc,2android,3ios
    private Byte orderChannel;

    //0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
    private Byte status;

    private Date createDate;

    private Date payDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getDeliverAddrId() {
        return deliverAddrId;
    }

    public void setDeliverAddrId(Long deliverAddrId) {
        this.deliverAddrId = deliverAddrId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Byte getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(Byte orderChannel) {
        this.orderChannel = orderChannel;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}