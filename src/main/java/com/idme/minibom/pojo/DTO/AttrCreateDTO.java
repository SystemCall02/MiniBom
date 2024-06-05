package com.idme.minibom.pojo.DTO;

import lombok.Data;

@Data
public class AttrCreateDTO {
    private String name;
    private String nameEn;
    private String description;
    private String descriptionEn;
    private String type;
    private Boolean enableFlag;

}
