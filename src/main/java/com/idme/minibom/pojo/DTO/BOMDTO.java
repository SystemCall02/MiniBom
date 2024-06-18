package com.idme.minibom.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BOMDTO {
    private Long sourceId;//partID
    private Long  targetId;//PartMasterID
    private BigDecimal quantity;
    private String referenceDes;
}
