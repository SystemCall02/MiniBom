package com.idme.minibom.pojo.DTO;

import lombok.Data;

@Data
public class AttributeQueryDTO {
    private String name;
    private Integer curPage;
    private Integer pageSize;
}
