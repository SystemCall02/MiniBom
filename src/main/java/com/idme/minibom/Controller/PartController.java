package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.delegate.exception.RdmDelegateException;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.PartQueryDTO;
import com.idme.minibom.pojo.DTO.PartVersionQueryDTO;
import com.idme.minibom.pojo.VO.PartQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "Part管理相关接口")
@RequestMapping("/idme/part")
@RestController
@CrossOrigin
public class PartController {
    @Autowired
    private PartDelegator partDelegator;

    @PostMapping("/create")
    @ApiOperation("创建Part")
    public Result create(@RequestBody PartCreateDTO dto) {
        return Result.success(partDelegator.create(dto));
    }

    @PostMapping("/delete")
    @ApiOperation("删除Part")
    public Result delete(@RequestBody MasterIdModifierDTO dto) {
        return Result.success(partDelegator.delete(dto));
    }

    @PostMapping("/update")
    @ApiOperation("更新Part")
    public Result update(@RequestBody PartUpdateDTO dto) {
        VersionCheckOutDTO versionCheckOutDTO = new VersionCheckOutDTO();
        versionCheckOutDTO.setMasterId(dto.getMaster().getId());
        PartViewDTO checkoutVO = partDelegator.checkout(versionCheckOutDTO);
        Long checkoutId = checkoutVO.getId();
        String name = checkoutVO.getMaster().getModifier();
        dto.setId(checkoutId);

        Result res;
        try {
            dto.setCreator(name);
            dto.setModifier(name);
            dto.getMaster().setCreator(name);
            dto.getMaster().setModifier(name);
            dto.getBranch().setCreator(name);
            dto.getBranch().setModifier(name);
            res = Result.success(partDelegator.update(dto));
        } catch (RdmDelegateException e) {
            VersionUndoCheckOutDTO versionUndoCheckOutDTO = new VersionUndoCheckOutDTO();
            versionUndoCheckOutDTO.setMasterId(dto.getMaster().getId());
            partDelegator.undoCheckout(versionUndoCheckOutDTO);
            throw e;
        }

        VersionCheckInDTO checkInDTO = new VersionCheckInDTO();
        checkInDTO.setMasterId(dto.getMaster().getId());
        partDelegator.checkin(checkInDTO);

        return res;
    }

    @PostMapping("/query")
    @ApiOperation("请求Part，如果id和name都为空，则返回所有Part")
    public Result query(@RequestBody PartQueryDTO dto) {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.setIsNeedTotal(true);
        boolean isNull = true;
        if (dto.id != null) {
            queryRequestVo.addCondition("id", ConditionType.EQUAL, dto.id);
            isNull = false;
        }
        if (dto.name != null) {
            queryRequestVo.addCondition("name", ConditionType.EQUAL, dto.name);
            isNull = false;
        }
        List<PartQueryViewDTO> resList = partDelegator.query(queryRequestVo, new RDMPageVO(1, 10000));
        long count = 0;

        if (isNull) {
            List<PartQueryViewDTO> newList = new ArrayList<>();
            for (PartQueryViewDTO partQueryViewDTO : resList) {
                if (partQueryViewDTO.getLatest()) {
                    count++;
                }
            }

            int start = (dto.curPage - 1) * dto.pageSize;
            int cur = 0;
            for (PartQueryViewDTO partQueryViewDTO : resList) {
                if (partQueryViewDTO.getLatest()) {
                    if (cur >= start) {
                        if (newList.size() < dto.pageSize) newList.add(partQueryViewDTO);
                    }
                    cur++;
                }
            }
            resList = newList;
        }

        PartQueryVO res = new PartQueryVO();
        res.setResList(resList);
        if (isNull) {
            res.setSize(count);
        } else {
            res.setSize((long) resList.size());
        }
        return Result.success(res);
    }

    @PostMapping("/allversions")
    @ApiOperation("获取Part某个大版本下的所有小版本")
    public Result allVersions(@RequestBody PartVersionQueryDTO dto) {
        VersionMasterDTO versionMasterDTO = new VersionMasterDTO();
        versionMasterDTO.setMasterId(dto.getMasterId());
        versionMasterDTO.setVersion(dto.getVersion());
        return Result.success(partDelegator.getAllVersions(versionMasterDTO, new RDMPageVO(dto.getCurPage(), dto.getPageSize())));
    }

    @PostMapping("/version")
    @ApiOperation("获取Part某个小版本信息")
    public Result version(@RequestBody PartVersionQueryDTO dto) {
        VersionMasterQueryDTO versionMasterQueryDTO = new VersionMasterQueryDTO();
        versionMasterQueryDTO.setMasterId(dto.getMasterId());
        versionMasterQueryDTO.setVersion(dto.getVersion());
        versionMasterQueryDTO.setIteration(dto.getIteration());
        return Result.success(partDelegator.getVersionByMaster(versionMasterQueryDTO));
    }

    @PostMapping("/delbranch")
    @ApiOperation("删除Part分支")
    public Result delVersion(@RequestBody PartVersionQueryDTO dto) {
        VersionMasterModifierDTO versionMasterModifierDTO = new VersionMasterModifierDTO();
        versionMasterModifierDTO.setMasterId(dto.getMasterId());
        versionMasterModifierDTO.setVersion(dto.getVersion());
        return Result.success(partDelegator.deleteBranch(versionMasterModifierDTO));
    }

    @PostMapping("/revise/{masterId}")
    @ApiOperation("修订Part(添加大版本)")
    public Result revise(@PathVariable Long masterId) {
        VersionReviseDTO versionReviseDTO = new VersionReviseDTO();
        versionReviseDTO.setMasterId(masterId);
        return Result.success(partDelegator.revise(versionReviseDTO));
    }
}
