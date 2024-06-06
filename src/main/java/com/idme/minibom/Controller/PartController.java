package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.san2.bean.enumerate.AssemblyMode;
import com.huawei.innovation.rdm.san2.bean.enumerate.PartSource;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartBranchUpdateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartMasterUpdateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartUpdateDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.PartModifyDTO;
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

    @PostMapping("/create")
    @ApiOperation("创建part")
    public Result create(@RequestBody PartCreateDTO dto) {
        return Result.success(partDelegator.create(dto));
    }

    @PostMapping("/delete")
    @ApiOperation("删除part")
    public Result delete(@RequestBody MasterIdModifierDTO dto) {
        return Result.success(partDelegator.delete(dto));
    }

    @PostMapping("/update")
    @ApiOperation("更新part")
    public Result update(@RequestBody PartModifyDTO dto) {
        PartUpdateDTO partUpdateDTO = new PartUpdateDTO();
        partUpdateDTO.setId(dto.getId());
        partUpdateDTO.setName(dto.getName());
        partUpdateDTO.setCreator(dto.getCreator());
        partUpdateDTO.setModifier(dto.getModifier());
        partUpdateDTO.setDescription(dto.getDescription());
        partUpdateDTO.setSource(PartSource.valueOf(dto.getSource()));
        partUpdateDTO.setPartType(AssemblyMode.valueOf(dto.getPartType()));
        partUpdateDTO.setKiaguid(dto.getKiaguid());
        PartMasterUpdateDTO master = new PartMasterUpdateDTO();
        master.setId(dto.getMaster().getId());
        master.setName(dto.getMaster().getName());
        master.setCreator(dto.getMaster().getCreator());
        master.setModifier(dto.getMaster().getModifier());
        partUpdateDTO.setMaster(master);
        PartBranchUpdateDTO branch = new PartBranchUpdateDTO();
        branch.setId(dto.getId());
        branch.setCreator(dto.getBranch().getCreator());
        branch.setModifier(dto.getBranch().getModifier());
        partUpdateDTO.setBranch(branch);
        return Result.success(partDelegator.update(partUpdateDTO));
    }


}
