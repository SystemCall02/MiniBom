package com.idme.minibom.pojo.Class;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
// 展示子项返回参数
public class BOM {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sourceId;
    private String sourceName;
    private BigDecimal quantity;
    private String referenceDes;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bomLinkId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bomUseOccurrenceId;
}
