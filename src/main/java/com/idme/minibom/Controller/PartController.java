package com.idme.minibom.Controller;

import com.alibaba.fastjson.JSONArray;
import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestCountVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.coresdk.extattrmgmt.dto.EXAValueParamDTO;
import com.huawei.innovation.rdm.dto.entity.ClassificationAttributeViewDTO;
import com.huawei.innovation.rdm.san2.bean.enumerate.AssemblyMode;
import com.huawei.innovation.rdm.san2.bean.enumerate.PartSource;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeCreateDTO;
import com.idme.minibom.pojo.Class.Classification;
import com.idme.minibom.pojo.Class.ExtAttr;
import com.idme.minibom.pojo.DTO.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Api(tags = "Part管理相关接口")
@RequestMapping
@RestController
public class PartController {
    @Autowired
    private PartDelegator partDelegator;

    @PostMapping("/createPart")
    public PartViewDTO create(@RequestBody PartNewDTO partNewDTO) {
        PartMasterCreateDTO partMaster = getPartMasterCreateDTO(partNewDTO.master);
        PartBranchCreateDTO partBranch = getPartBranchCreateDTO(partNewDTO.branch);
        PartCreateDTO dto = new PartCreateDTO();
        dto.setId(partNewDTO.id);
        dto.setModifier(partNewDTO.modifier);
        dto.setCreator(partNewDTO.creator);
        dto.setName(partNewDTO.name);
        dto.setDescription(partNewDTO.description);
        dto.setMaster(partMaster);
        dto.setBranch(partBranch);
        dto.setPartType(getPartType(partNewDTO.partType));
        dto.setSource(getPartSource(partNewDTO.source));
        dto.setExtAttrs(getExtAttrs(partNewDTO.extAttrs.get(0)));
        ClassificationAttributeViewDTO clsAttrDto = new ClassificationAttributeViewDTO();

        dto.setClsAttrs(jsonArray);
        return partDelegator.create(dto);
    }

    /**
     * 删除Part
     *
     * @return 失败返回0，成功返回1
     */
    @PostMapping("/deletePart")
    public int delete(@RequestBody PartDeleteDTO partDeleteDto) {
        MasterIdModifierDTO dto = new MasterIdModifierDTO();
        dto.setModifier(partDeleteDto.modifier);
        dto.setMasterId(partDeleteDto.masterId);
        return partDelegator.delete(dto);
    }

    /**
     * 获取Part
     *
     * @return 对应Part
     */
    @PostMapping("/getPart")
    public PartViewDTO get(@RequestBody PartGetDTO partGetDTO) {
        PersistObjectIdDecryptDTO dto = new PersistObjectIdDecryptDTO();
        dto.setId(partGetDTO.id);
        dto.setDecrypt(partGetDTO.decrypt);
        return partDelegator.get(dto);
    }

    /**
     * 通过id查询Part（后续如果需要添加查询条件还可再补充）
     *
     * @return 查询到的Part列表
     */
    @PostMapping("/queryPart")
    public List<PartQueryViewDTO> query(@RequestBody PartQueryDTO PartQueryDTO) {
        QueryCondition condition = new QueryCondition("id", QueryCondition.EQUAL, Long.toString(PartQueryDTO.id));
        ArrayList<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        QueryRequestVo vo = new QueryRequestVo();
        vo.setConditions(conditions);
        RDMPageVO pageVO = new RDMPageVO();
        return partDelegator.query(vo, pageVO);
    }

    /**
     * 统计Part数量
     *
     * @return Part总数
     */
    @PostMapping("/countPart")
    public long count() {
        QueryRequestCountVo vo = new QueryRequestCountVo();
        return partDelegator.count(vo);
    }

    private PartMasterCreateDTO getPartMasterCreateDTO(PartMasterNewDTO partMasterNewDTO) {
        PartMasterCreateDTO partMaster = new PartMasterCreateDTO();
        partMaster.setId(partMasterNewDTO.id);
        partMaster.setModifier(partMasterNewDTO.modifier);
        partMaster.setCreator(partMasterNewDTO.creator);
        partMaster.setName(partMasterNewDTO.name);
        return partMaster;
    }

    private PartBranchCreateDTO getPartBranchCreateDTO(PartBranchNewDTO partBranchNewDTO) {
        PartBranchCreateDTO partBranch = new PartBranchCreateDTO();
        partBranch.setId(partBranchNewDTO.id);
        partBranch.setModifier(partBranchNewDTO.modifier);
        partBranch.setCreator(partBranchNewDTO.creator);
        return partBranch;
    }

    private AssemblyMode getPartType(String part_type) {
        if (part_type.equals("Separable") || part_type.equals("可分离")) {
            return AssemblyMode.Separable;
        } else if (part_type.equals("Inseparable") || part_type.equals("不可分离")) {
            return AssemblyMode.Inseparable;
        } else if (part_type.equals("Part") || part_type.equals("零件")) {
            return AssemblyMode.Part;
        }

        return null;
    }

    private PartSource getPartSource(String part_source) {
        if (part_source.equals("制造") || part_source.equals("Make")) {
            return PartSource.Make;
        } else if (part_source.equals("购买") || part_source.equals("Buy")) {
            return PartSource.Buy;
        } else if (part_source.equals("购买-单一供应源") || part_source.equals("Buy_SingleSource")) {
            return PartSource.Buy_SingleSource;
        }

        return null;
    }

    private ArrayList<EXAValueParamDTO> getExtAttrs(ExtAttr extAttr) {
        ArrayList<EXAValueParamDTO> attrs = new ArrayList<>();
        EXAValueParamDTO eaValueParamDTO = new EXAValueParamDTO(extAttr.name, extAttr.value);
        attrs.add(eaValueParamDTO);
        return attrs;
    }
}
