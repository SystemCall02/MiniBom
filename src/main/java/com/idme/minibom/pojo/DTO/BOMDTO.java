package com.idme.minibom.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
//BOM 创建入参
public class BOMDTO {
    private Long sourceId;//partID
    private Long  targetId;//PartMasterID
    private BigDecimal quantity;
    private String referenceDes;
}
