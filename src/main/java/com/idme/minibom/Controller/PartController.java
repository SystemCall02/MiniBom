package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.bean.enumerate.AssemblyMode;
import com.huawei.innovation.rdm.san2.bean.enumerate.PartSource;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.PartModifyDTO;
import com.idme.minibom.pojo.DTO.PartQueryDTO;
import com.idme.minibom.pojo.DTO.PartVersionQueryDTO;
import com.idme.minibom.pojo.VO.PartQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Part管理相关接口")
@RequestMapping("/idme/part")
@RestController
@CrossOrigin
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

    @PostMapping("/checkout")
    @ApiOperation("检出part")
    public Result checkout(@RequestBody VersionCheckOutDTO dto) {
        return Result.success(partDelegator.checkout(dto));
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

    @PostMapping("/query")
    @ApiOperation("请求part")
    public Result query(@RequestBody PartQueryDTO dto) {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        if (dto.id == null && dto.name == null) {
            queryRequestVo.setIsNeedTotal(true);
        }
        if (dto.id != null) {
            queryRequestVo.addCondition("id", ConditionType.EQUAL, dto.id);
        }
        if (dto.name != null) {
            queryRequestVo.addCondition("name", ConditionType.LIKE, dto.name);
        }
        List<PartQueryViewDTO> resList = partDelegator.query(queryRequestVo, new RDMPageVO(dto.curPage, dto.pageSize));
        PartQueryVO res = new PartQueryVO();
        res.setResList(resList);
        res.setSize(partDelegator.count(queryRequestVo));
        return Result.success(res);
    }

    @PostMapping("/allversions")
    @ApiOperation("获取Part所有版本")
    public Result allVersions(@RequestBody PartVersionQueryDTO dto) {
        VersionMasterDTO versionMasterDTO = new VersionMasterDTO();
        versionMasterDTO.setMasterId(dto.getMasterId());
        return Result.success(partDelegator.getAllVersions(versionMasterDTO, new RDMPageVO(dto.getCurPage(), dto.getPageSize())));
    }

    @PostMapping("/version")
    @ApiOperation("获取Part对应版本信息")
    public Result version(@RequestBody PartVersionQueryDTO dto) {
        VersionMasterQueryDTO versionMasterQueryDTO = new VersionMasterQueryDTO();
        versionMasterQueryDTO.setMasterId(dto.getMasterId());
        versionMasterQueryDTO.setVersion(dto.getVersion());
        return Result.success(partDelegator.getVersionByMaster(versionMasterQueryDTO));
    }

    @PostMapping("/Delversion")
    @ApiOperation("删除Part某一小版本")
    public Result delVersion(@RequestBody PartVersionQueryDTO dto) {
        VersionMasterModifierDTO versionMasterModifierDTO = new VersionMasterModifierDTO();
        versionMasterModifierDTO.setMasterId(dto.getMasterId());
        versionMasterModifierDTO.setVersion(dto.getVersion());
        return Result.success(partDelegator.deleteBranch(versionMasterModifierDTO));
    }
}
