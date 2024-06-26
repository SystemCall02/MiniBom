package com.idme.minibom.pojo.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
//BOM 创建入参
public class BOMDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sourceId;//partID


    private Long  targetId;//PartMasterID
    private BigDecimal quantity;
    private String referenceDes;
}
