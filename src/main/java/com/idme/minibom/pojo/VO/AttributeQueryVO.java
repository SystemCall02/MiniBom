package com.idme.minibom.pojo.VO;

import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionQueryViewDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionViewDTO;
import lombok.Data;

import java.util.List;

@Data
public class AttributeQueryVO {
    private List<EXADefinitionViewDTO> resultList;
    private Long total;
}
