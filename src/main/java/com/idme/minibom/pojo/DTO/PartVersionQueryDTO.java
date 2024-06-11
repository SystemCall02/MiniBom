package com.idme.minibom.pojo.DTO;

import lombok.Data;

@Data
public class PartVersionQueryDTO {
    private Long masterId;
    private String version;
    private Integer curPage;
    private Integer pageSize;
}
