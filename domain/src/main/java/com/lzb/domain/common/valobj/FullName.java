package com.lzb.domain.common.valobj;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 姓名<br/>
 * Created on : 2023-08-31 12:53
 * @author lizebin
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class FullName implements Serializable {

    private final String firstName;
    private final String lastName;

}
