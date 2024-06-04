package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.san2.bean.enumerate.AssemblyMode;
import com.huawei.innovation.rdm.san2.bean.enumerate.PartSource;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartBranchCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartMasterCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.idme.minibom.pojo.DTO.PartDeleteDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Part管理相关接口")
@RequestMapping
@RestController
public class PartController {
    @Autowired
    private PartDelegator partDelegator;

    @PostMapping("/createPart")
    public PartViewDTO create(@RequestBody PartCreateDTO partDTO) {
        return partDelegator.create(partDTO);
    }

    @PostMapping("/deletePart")
    public int delete(@RequestBody PartDeleteDto partDeleteDto) {
        MasterIdModifierDTO dto = new MasterIdModifierDTO();
        dto.setModifier(partDeleteDto.modifier);
        dto.setMasterId(partDeleteDto.masterId);
        return partDelegator.delete(dto);
    }
}
