package com.lzb.infr.order.converter;

import java.math.BigDecimal;

import com.lzb.BaseUnitTest;
import com.lzb.domain.order.aggregate.OrderAddress;
import com.lzb.domain.order.aggregate.OrderDetail;
import com.lzb.domain.order.enums.OrderStatus;
import com.lzb.infr.domain.order.converter.OrderConverter;
import com.lzb.infr.domain.order.persistence.po.OrderDetailPo;
import com.lzb.infr.domain.order.persistence.po.OrderPo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderConverterUnitTest extends BaseUnitTest {

    @org.junit.jupiter.api.Test
    void toOrder() {
    }

    @org.junit.jupiter.api.Test
    void toOrderDetails() {
    }

    @Test
    @DisplayName("测试字段映射")
    void toOrderDetail() {

        OrderDetailPo orderDetailPo = new OrderDetailPo();
        orderDetailPo.setId(0L);
        orderDetailPo.setOrderId(0L);
        orderDetailPo.setSkuId(0);
        orderDetailPo.setOrderStatus(OrderStatus.WAIT_PAY);
        orderDetailPo.setPrice(BigDecimal.TEN);

        OrderDetail orderDetail = OrderConverter.toOrderDetail(orderDetailPo);
        assertJSON(orderDetail);
    }

    @Test
    @DisplayName("测试订单地址字段映射")
    void toOrderAddress() {

        OrderPo orderPo = new OrderPo();
        orderPo.setOrderId(888L);
        orderPo.setEmail("thisIsEmail@qq.com");
        orderPo.setPhoneNumber("123456");
        orderPo.setFirstName("firstName");
        orderPo.setLastName("lastName");
        orderPo.setAddressLine1("addressLine1");
        orderPo.setAddressLine2("addreaaLine2");
        orderPo.setCountry("country");

        OrderAddress orderAddress = OrderConverter.toOrderAddress(orderPo);
        assertJSON(orderAddress);

    }
}