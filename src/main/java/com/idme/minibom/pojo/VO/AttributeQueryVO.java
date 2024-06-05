package com.idme.minibom.pojo.VO;

import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionQueryViewDTO;
import lombok.Data;

import java.util.List;

@Data
public class AttributeQueryVO {
    private List<EXADefinitionQueryViewDTO> resultList;
    private Long total;
}
