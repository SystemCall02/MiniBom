package com.idme.minibom.pojo.VO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassificationTreeVO {
    private Long id;
    private String name;
    private String businessCode;
    private String nameEn;
    private Boolean enableFlag;
    private List<ClassificationTreeVO> children;
}
