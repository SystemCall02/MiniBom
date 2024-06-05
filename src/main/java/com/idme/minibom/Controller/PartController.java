package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.idme.minibom.pojo.DTO.PartDeleteDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Part管理相关接口")
@RequestMapping("/idme/part")
@RestController
public class PartController {
    @Autowired
    private PartDelegator partDelegator;

    /**
     * 创建part
     * @param partDTO
     * @return
     */
    @PostMapping("/createPart")
    @ApiOperation("创建part")
    public PartViewDTO create(@RequestBody PartCreateDTO partDTO) {
        return partDelegator.create(partDTO);
    }

    /**
     * 删除part
     * @param partDeleteDto
     * @return
     */
    @PostMapping("/deletePart")
    @ApiOperation("删除part")
    public int delete(@RequestBody PartDeleteDTO partDeleteDto) {
        MasterIdModifierDTO dto = new MasterIdModifierDTO();
        dto.setModifier(partDeleteDto.modifier);
        dto.setMasterId(partDeleteDto.masterId);
        return partDelegator.delete(dto);
    }
}
