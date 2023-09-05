package com.lzb.domain.order.aggregate;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.lzb.domain.common.BaseAggregate;
import com.lzb.domain.order.enums.OrderStatus;
import com.lzb.domain.order.event.OrderCanceledEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单聚合根<br/>
 * 实体所有写的操作，必须通过聚合根调用，所以改成包可见(default)
 * 实体所有读操作，可以改成公共(public)
 * Created on : 2022-06-11 14:41
 *
 * @author lizebin
 */
@Slf4j
@Getter
// builder 反序列化
@SuperBuilder
@Jacksonized
public class Order extends BaseAggregate<Order> {

    @NonNull
    private OrderStatus orderStatus;

    @NonNull
    private String currency;

    @NonNull
    private BigDecimal exchangeRate;

    @NonNull
    private BigDecimal totalShouldPay;

    @NonNull
    private BigDecimal totalActualPay;

    @NonNull
    private OrderAddress orderAddress;

    @Builder.Default
    private OrderDetails orderDetails = new OrderDetails(new ArrayList<>());

    public void updateTotalActualPay(BigDecimal totalActualPay) {
        this.totalActualPay = totalActualPay;
    }

    /**
     * 取消订单
     */
    public void cancel() {
        orderStatus = OrderStatus.CANCELED;
        orderDetails.forEach(OrderDetail::cancel);
        addEvent(OrderCanceledEvent.create(id));
    }

    /**
     * 是否取消
     * @return
     */
    public boolean isCancel() {
        return OrderStatus.CANCELED == orderStatus;
    }
}
