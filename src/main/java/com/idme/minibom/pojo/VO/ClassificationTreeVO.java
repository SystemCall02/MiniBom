package com.idme.minibom.pojo.VO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassificationTreeVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    private String name;
    private String businessCode;
    private String nameEn;
    private Boolean enableFlag;
    private List<ClassificationTreeVO> children;
}
