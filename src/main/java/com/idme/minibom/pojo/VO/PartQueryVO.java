package com.idme.minibom.pojo.VO;

import com.huawei.innovation.rdm.san2.dto.entity.PartQueryViewDTO;
import lombok.Data;
import java.util.List;

@Data
public class PartQueryVO {
    private List<PartQueryViewDTO> resList;
    private Long size;
}
