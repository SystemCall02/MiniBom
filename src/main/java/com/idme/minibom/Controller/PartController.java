package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.MasterIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestCountVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartQueryViewDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.idme.minibom.pojo.DTO.PartDeleteDTO;
import com.idme.minibom.pojo.DTO.PartGetDTO;
import com.idme.minibom.pojo.VO.PartQueryVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public List<PartQueryViewDTO> query(@RequestBody PartQueryVO partQueryVO) {
        QueryCondition condition = new QueryCondition("id", QueryCondition.EQUAL, Long.toString(partQueryVO.id));
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
}
