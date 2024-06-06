package com.idme.minibom.pojo.DTO;

import lombok.Data;

@Data
public class PartModifyDTO {
    Long id;
    String name;
    String creator;
    String modifier;
    String description;
    String source;
    String partType;
    String kiaguid;
    PartMasterModifyDTO master;
    PartBranchModifyDTO branch;
}
