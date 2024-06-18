package com.idme.minibom.pojo.Class;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class BOM {
    private Long targetId;
    private String targetName;
    private BigDecimal quantity;
    private String referenceDes;
}
