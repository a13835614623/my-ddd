package com.lzb.infr.order.persistence.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lzb.BaseIntegrationTest;
import com.lzb.component.utils.DateUtils;
import com.lzb.infr.order.persistence.po.OrderPo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <br/>
 * Created on : 2023-08-30 08:34
 * @author mac
 */
class OrderPoServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private IOrderPoService orderPoService;

    @Test
    @DisplayName("测试订单条数")
    void should_count_from_order() {
        assertThat(orderPoService.count()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("插入设置addTime和updateTime")
    void should_fill_add_time_and_update_time() {
        OrderPo orderPo = new OrderPo();
        orderPo.setOrderStatus("WAIT_AUTH");
        orderPo.setCurrency("CNY");
        orderPo.setExchangeRate(BigDecimal.ZERO);
        orderPo.setTotalShouldPay(BigDecimal.ZERO);
        orderPo.setTotalActualPay(BigDecimal.ZERO);
        // 自动填充update_time/add_time
        boolean a = orderPoService.save(orderPo);
        assertThat(a).isTrue();

        OrderPo o = orderPoService.getById(orderPo.getOrderId());
        assertThat(o.getAddTime()).isNotNull();
        assertThat(o.getUpdateTime()).isNotNull();

    }

    @Test
    @DisplayName("update null")
    void should_update_null() {

        OrderPo orderPo = new OrderPo();
        orderPo.setOrderStatus("WAIT_AUTH");
        orderPo.setCurrency("CNY");
        orderPo.setExchangeRate(BigDecimal.ZERO);
        orderPo.setTotalShouldPay(BigDecimal.ZERO);
        orderPo.setTotalActualPay(BigDecimal.ZERO);
        boolean a = orderPoService.save(orderPo);
        assertThat(a).isTrue();

        long orderId = orderPo.getOrderId();
        LocalDateTime addTime = orderPoService.getById(orderId).getAddTime();
        LocalDateTime updateTime = orderPoService.getById(orderId).getUpdateTime();

        // 更新update_time，不更新add_time
        OrderPo orderPo1 = new OrderPo();
        orderPo1.setOrderId(orderId);
        orderPo1.setOrderStatus("WAIT_AUTH");
        orderPo1.setCurrency("CNY");
        boolean updateFlag = orderPoService.updateById(orderPo1);
        assertThat(updateFlag).isTrue();

        OrderPo o = orderPoService.getById(orderId);
        assertThat(o.getTotalShouldPay()).isNull();
        // addTime不变
        assertThat(DateUtils.toUnix(o.getAddTime())).isEqualTo(DateUtils.toUnix(addTime));
        // updateTime更新
        assertThat(DateUtils.toUnix(o.getUpdateTime())).isGreaterThanOrEqualTo(DateUtils.toUnix(updateTime));
    }

    @Test
    @DisplayName("批量更新自动填充update_time，不改变add_time，字段可以设置null")
    void should_batch_update() {

        batchInsert();

        List<OrderPo> list = orderPoService.list();
        Map<Long, Long> orderId2UpdateTime = new HashMap<>();
        Map<Long, Long> orderId2AddTime = new HashMap<>();

        for (OrderPo orderPo : list) {
            orderPo.setTotalShouldPay(null);
            orderPo.setExchangeRate(null);
            orderPo.setTotalActualPay(null);
            orderId2UpdateTime.put(orderPo.getOrderId(), DateUtils.toUnix(orderPo.getUpdateTime()));
            orderId2AddTime.put(orderPo.getOrderId(), DateUtils.toUnix(orderPo.getAddTime()));
        }

        orderPoService.updateBatchById(list);

        List<OrderPo> newList = orderPoService.list();
        for (OrderPo orderPo : newList) {
            assertThat(orderPo.getTotalShouldPay()).isNull();
            assertThat(orderPo.getExchangeRate()).isNull();
            assertThat(orderPo.getTotalActualPay()).isNull();
            assertThat(DateUtils.toUnix(orderPo.getAddTime())).isEqualTo(orderId2AddTime.get(orderPo.getOrderId()));
            assertThat(DateUtils.toUnix(orderPo.getUpdateTime())).isGreaterThanOrEqualTo(orderId2UpdateTime.get(orderPo.getOrderId()));
        }

    }

    private void batchInsert() {
        List<OrderPo> list = new ArrayList<>();

        OrderPo orderPo = new OrderPo();
        orderPo.setOrderStatus("WAIT_AUTH");
        orderPo.setCurrency("CNY");
        orderPo.setExchangeRate(BigDecimal.ZERO);
        orderPo.setTotalShouldPay(BigDecimal.ZERO);
        orderPo.setTotalActualPay(BigDecimal.ZERO);
        list.add(orderPo);

        OrderPo orderPo1 = new OrderPo();
        orderPo1.setOrderStatus("WAIT_AUTH");
        orderPo1.setCurrency("CNY");
        orderPo1.setExchangeRate(BigDecimal.ONE);
        orderPo1.setTotalShouldPay(BigDecimal.ONE);
        orderPo1.setTotalActualPay(BigDecimal.ONE);
        list.add(orderPo1);

        orderPoService.saveBatch(list);
    }

}
