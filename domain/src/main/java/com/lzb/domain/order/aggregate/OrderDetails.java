package com.lzb.domain.order.aggregate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.lzb.domain.common.aggregate.Identified;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

/**
 * 订单聚合根关联对象-订单明细<br/>
 * 读方法：public
 * 写方法：default
 * Created on : 2022-03-07 19:57
 *
 * @author lizebin
 */
@Slf4j
public class OrderDetails implements Iterable<OrderDetail>, Serializable, Identified<OrderDetail> {

    private final List<OrderDetail> list;

    /**
     * @JsonCreator 用于反序列化
     * @param list
     */
    @JsonCreator
    public OrderDetails(List<OrderDetail> list) {
        this.list = list;
        validate();
    }

    void validate() {
        Assert.notEmpty(this.list, "订单明细不能为空");
        if (isDuplicated()) {
            throw new IllegalArgumentException("订单明细id重复");
        }
    }

    @Override
    public Iterator<OrderDetail> iterator() {
        return list.iterator();
    }

    public Stream<OrderDetail> toStream() {
        return list.stream();
    }

    @Override
    public Collection<OrderDetail> getCollection() {
        return list;
    }

    public Set<Integer> getSkuIds() {
        return StreamEx.of(list).map(OrderDetail::getSkuId).toSet();
    }

    public int count() {
        return list.size();
    }
}