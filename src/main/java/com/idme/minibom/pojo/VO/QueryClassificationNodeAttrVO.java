package com.idme.minibom.pojo.VO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class QueryClassificationNodeAttrVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String nameEn;
    private String description;
    private String descriptionEn;
}
