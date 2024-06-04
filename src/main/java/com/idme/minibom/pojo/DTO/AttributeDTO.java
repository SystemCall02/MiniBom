package com.idme.minibom.pojo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class AttributeDTO {
    private String name;
    private Integer curPage;
    private Integer pageSize;
}
