package com.lzb.app.cmd;

import com.lzb.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 生单<br/>
 * Created on : 2023-09-05 19:53
 * @author lizebin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class PlaceOrderService {

    private final OrderRepository orders;




}