package com.lzb.domain.order.dto;

/**
 * 更新订单地址<br/>
 * Created on : 2023-09-16 10:29
 * @author lizebin
 */
public record UpdateOrderAddressDto(long orderId, String email, String phoneNumber, String firstName,
                                    String lastName, String addressLine1, String addressLine2, String country) {

}