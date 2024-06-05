package com.idme.minibom.pojo.DTO;

import lombok.Data;

@Data
public class ClassificationQueryDTO {
    private String name;
    private Integer curPage;
    private Integer pageSize;
}
