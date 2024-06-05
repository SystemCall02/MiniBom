package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.idme.minibom.pojo.DTO.PartDeleteDTO;
import com.idme.minibom.pojo.DTO.PartGetDTO;
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
    public int delete(@RequestBody PartDeleteDTO partDeleteDto) {
        MasterIdModifierDTO dto = new MasterIdModifierDTO();
        dto.setModifier(partDeleteDto.modifier);
        dto.setMasterId(partDeleteDto.masterId);
        return partDelegator.delete(dto);
    }

    @PostMapping("/getPart")
    public PartViewDTO get(@RequestBody PartGetDTO partGetDTO) {
        PersistObjectIdDecryptDTO dto = new PersistObjectIdDecryptDTO();
        dto.setId(partGetDTO.id);
        dto.setDecrypt(partGetDTO.decrypt);
        return partDelegator.get(dto);
    }
}
