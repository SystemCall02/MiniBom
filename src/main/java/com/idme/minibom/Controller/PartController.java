package com.idme.minibom.Controller;

import com.alibaba.fastjson.JSONArray;
import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.san2.bean.enumerate.AssemblyMode;
import com.huawei.innovation.rdm.san2.bean.enumerate.PartSource;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartBranchCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartMasterCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.idme.minibom.pojo.DTO.PartDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@Api(tags = "Part管理相关接口")
@RequestMapping
@RestController
public class PartController {
    @Autowired
    private PartDelegator partDelegator;


    public PartViewDTO create(@RequestBody PartDTO partDTO) {
        PartCreateDTO partCreateDTO = new PartCreateDTO();
        partCreateDTO.setId(partDTO.getId());
        partCreateDTO.setModifier(partDTO.getModifier());
        partCreateDTO.setLastUpdateTime(partDTO.getLast_update_time());
        partCreateDTO.setCreator(partDTO.getCreator());
        partCreateDTO.setCreateTime(partDTO.getCreate_time());
        partCreateDTO.setRdmExtensionType(partDTO.getRdm_ext_type());
        ObjectReferenceParamDTO tenant = new ObjectReferenceParamDTO(partDTO.getTenant_id(), partDTO.getTenant_clazz());
        partCreateDTO.setTenant(tenant);
        partCreateDTO.setName(partDTO.getName());
        partCreateDTO.setDescription(partDTO.getDescription());
        partCreateDTO.setSecurityLevel(partDTO.getSecurity_level());
        partCreateDTO.setKiaguid(partDTO.getKiaguid());
        partCreateDTO.setWorkingCopy(partDTO.isWorking_copy());
        partCreateDTO.setCheckOutUserName(partDTO.getCheckout_username());
        partCreateDTO.setCheckOutTime(partDTO.getCheckout_time());
        AssemblyMode part_type;
        String enName = partDTO.getPart_enName();
        if (enName.equals("Separable")) {
            part_type = AssemblyMode.Separable;
        } else if (enName.equals("Inseparable")) {
            part_type = AssemblyMode.Inseparable;
        } else {
            part_type = AssemblyMode.Part;
        }
        partCreateDTO.setPartType(part_type);
        partCreateDTO.setIterationNote(partDTO.getIteration_note());
        PartSource part_source;
        String source = partDTO.getSource();
        if (source.equals("制造") || source.equals("Make")) {
            part_source = PartSource.Make;
        } else if (source.equals("购买") || source.equals("Buy")) {
            part_source = PartSource.Buy;
        } else {
            part_source = PartSource.Buy_SingleSource;
        }

        partCreateDTO.setMaster(getPartMasterCreateDTO(partDTO, tenant));
        partCreateDTO.setBranch(getPartBranchCreateDTO(partDTO, tenant));

        return partDelegator.create(partCreateDTO);
    }

    private static PartMasterCreateDTO getPartMasterCreateDTO(PartDTO partDTO, ObjectReferenceParamDTO tenant) {
        PartMasterCreateDTO partMasterCreateDTO = new PartMasterCreateDTO();
        partMasterCreateDTO.setId(partDTO.getId());
        partMasterCreateDTO.setModifier(partDTO.getModifier());
        partMasterCreateDTO.setLastUpdateTime(partDTO.getLast_update_time());
        partMasterCreateDTO.setCreator(partDTO.getCreator());
        partMasterCreateDTO.setCreateTime(partDTO.getCreate_time());
        partMasterCreateDTO.setRdmExtensionType(partDTO.getRdm_ext_type());
        partMasterCreateDTO.setTenant(tenant);
        partMasterCreateDTO.setName(partDTO.getName());
        partMasterCreateDTO.setEndPart(partDTO.isEnd_part());
        partMasterCreateDTO.setNumber(partDTO.getNumber());
        partMasterCreateDTO.setPhantomPart(partDTO.isPhantom_part());
        return partMasterCreateDTO;
    }

    private static PartBranchCreateDTO getPartBranchCreateDTO(PartDTO partDTO, ObjectReferenceParamDTO tenant) {
        PartBranchCreateDTO partBranchCreateDTO = new PartBranchCreateDTO();
        partBranchCreateDTO.setId(partDTO.getId());
        partBranchCreateDTO.setModifier(partDTO.getModifier());
        partBranchCreateDTO.setLastUpdateTime(partDTO.getLast_update_time());
        partBranchCreateDTO.setCreator(partDTO.getCreator());
        partBranchCreateDTO.setCreateTime(partDTO.getCreate_time());
        partBranchCreateDTO.setRdmExtensionType(partDTO.getRdm_ext_type());
        partBranchCreateDTO.setTenant(tenant);
        return partBranchCreateDTO;
    }
}
