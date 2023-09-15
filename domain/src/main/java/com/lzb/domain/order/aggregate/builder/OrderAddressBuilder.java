package com.lzb.domain.order.aggregate.builder;

import com.lzb.component.helper.SpringHelper;
import com.lzb.domain.common.aggregate.BaseBuilder;
import com.lzb.domain.order.aggregate.OrderAddress;
import com.lzb.domain.order.valobj.FullAddressLine;
import com.lzb.domain.order.valobj.FullName;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <br/>
 * Created on : 2023-09-15 22:08
 * @author lizebin
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class OrderAddressBuilder extends BaseBuilder<OrderAddress> {

    private long id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String country;

    public OrderAddressBuilder id(long id) {
        this.id = id;
        return this;
    }

    public OrderAddressBuilder email(String email) {
        this.email = email;
        return this;
    }

    public OrderAddressBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public OrderAddressBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public OrderAddressBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public OrderAddressBuilder addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public OrderAddressBuilder addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public OrderAddressBuilder country(String country) {
        this.country = country;
        return this;
    }

    public static OrderAddressBuilder newInstance() {
        return SpringHelper.getBean(OrderAddressBuilder.class);
    }

    @Override
    protected OrderAddress doBuild() {
        return new OrderAddress(id,
                new FullName(firstName, lastName),
                new FullAddressLine(addressLine1, addressLine2),
                email, phoneNumber, country);
    }
}
