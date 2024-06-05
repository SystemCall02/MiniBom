package com.idme.minibom.pojo.VO;

import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeQueryViewDTO;
import lombok.Data;

import java.util.List;

@Data
public class ClassificationQueryVO {
    private List<ClassificationNodeQueryViewDTO> resultList;
    private Long total;
}
