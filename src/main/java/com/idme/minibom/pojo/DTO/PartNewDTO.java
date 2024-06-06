package com.idme.minibom.pojo.DTO;

import com.idme.minibom.pojo.Class.Classification;
import com.idme.minibom.pojo.Class.ExtAttr;
import lombok.Data;

import java.util.ArrayList;

@Data
public class PartNewDTO {
    public long id;
    public String modifier;
    public String creator;
    public String name;
    public String description;
    public PartMasterNewDTO master;
    public PartBranchNewDTO branch;
    public String partType;
    public String source;
    public ArrayList<Classification> clsAttrs;
    public ArrayList<ExtAttr> extAttrs;
}

