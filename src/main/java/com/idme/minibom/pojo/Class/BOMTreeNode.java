package com.idme.minibom.pojo.Class;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//BOM树节点
@Getter
@Setter
public class BOMTreeNode {
    private Long partMasterId;
    private List<BOMTreeNode> children;
    public BOMTreeNode(Long partMasterId) {
        this.partMasterId = partMasterId;
        this.children = new ArrayList<>();
    }
    public void addChild(BOMTreeNode child) {
        children.add(child);
    }
}
