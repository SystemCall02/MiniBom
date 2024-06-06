package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestCountVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "Part管理相关接口")
@RequestMapping("/idme/Part")
@RestController
public class PartController {
    @Autowired
    private PartDelegator partDelegator;

    @PostMapping("/create")
    public Result create(@RequestBody PartCreateDTO partCreateDTO) {
        PartViewDTO resDto = partDelegator.create(partCreateDTO);
        return Result.success(resDto);
    }

    /**
     * 删除Part
     *
     * @return 失败返回0，成功返回1
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody PartDeleteDTO partDeleteDto) {
        MasterIdModifierDTO dto = new MasterIdModifierDTO();
        dto.setModifier(partDeleteDto.modifier);
        dto.setMasterId(partDeleteDto.masterId);
        int res = partDelegator.delete(dto);
        return Result.success(res);
    }

    /**
     * 获取Part
     *
     * @return 对应Part
     */
    @PostMapping("/get")
    public Result get(@RequestBody PartGetDTO partGetDTO) {
        PersistObjectIdDecryptDTO dto = new PersistObjectIdDecryptDTO();
        dto.setId(partGetDTO.id);
        dto.setDecrypt(partGetDTO.decrypt);
        PartViewDTO resDto = partDelegator.get(dto);
        return Result.success(resDto);
    }

    /**
     * 通过id查询Part（后续如果需要添加查询条件还可再补充）
     *
     * @return 查询到的Part列表
     */
    @PostMapping("/query")
    public Result query(@RequestBody PartQueryDTO PartQueryDTO) {
        QueryCondition condition = new QueryCondition("id", QueryCondition.EQUAL, Long.toString(PartQueryDTO.id));
        ArrayList<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        QueryRequestVo vo = new QueryRequestVo();
        vo.setConditions(conditions);
        RDMPageVO pageVO = new RDMPageVO();
        List<PartQueryViewDTO> resDtoList = partDelegator.query(vo, pageVO);
        return Result.success(resDtoList);
    }

    /**
     * 统计Part数量
     *
     * @return Part总数
     */
    @PostMapping("/count")
    public Result count() {
        long res = partDelegator.count(new QueryRequestCountVo());
        return Result.success(res);
    }
}
