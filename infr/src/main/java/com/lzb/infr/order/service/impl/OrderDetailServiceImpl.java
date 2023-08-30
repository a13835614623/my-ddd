package com.lzb.infr.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzb.infr.order.entity.OrderDetailDo;
import com.lzb.infr.order.mapper.OrderDetailMapper;
import com.lzb.infr.order.service.IOrderDetailService;

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
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetailDo> implements IOrderDetailService {

}
