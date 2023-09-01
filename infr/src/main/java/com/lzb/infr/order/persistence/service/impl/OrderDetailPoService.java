package com.lzb.infr.order.persistence.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzb.infr.order.persistence.po.OrderDetailPo;
import com.lzb.infr.order.persistence.mapper.OrderDetailPoMapper;
import com.lzb.infr.order.persistence.service.IOrderDetailPoService;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lizebin
 * @since 2023-08-30
 */
@Service
public class OrderDetailPoService extends ServiceImpl<OrderDetailPoMapper, OrderDetailPo> implements IOrderDetailPoService {

    @Override
    public List<OrderDetailPo> listByOrderId(long id) {
        LambdaQueryWrapper<OrderDetailPo> query = Wrappers.lambdaQuery(OrderDetailPo.class);
        query.eq(OrderDetailPo::getOrderId, id);
        return list(query);
    }
}