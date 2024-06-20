package com.idme.minibom.pojo.Class;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//BOM树节点
@Getter
@Setter
/*
* 可加入更多参数
* 或直接根据其中的ID来get其他参数
* */
public class BOMTreeNode {
    private Long partMasterId;
    private List<BOMTreeNode> children;
    private String partMasterName="";
    private String partMasterNumber="";
    public BOMTreeNode(Long partMasterId,String partMasterName,String partMasterNumber) {
        this.partMasterId = partMasterId;
        this.partMasterName =partMasterName;
        this.partMasterNumber = partMasterNumber;
        this.children = new ArrayList<>();
    }
    public void addChild(BOMTreeNode child) {
        children.add(child);
    }
}
