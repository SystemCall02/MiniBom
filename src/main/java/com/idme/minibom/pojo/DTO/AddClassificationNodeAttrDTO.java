package com.idme.minibom.pojo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AddClassificationNodeAttrDTO {
    private Long holderId;
    private List<Long> attrIds;
}
