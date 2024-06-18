package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.GenericLinkQueryDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.delegator.BOMUsesOccurrenceDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceViewDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkCreateDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.Class.BOM;
import com.idme.minibom.pojo.DTO.BOMDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "BOM管理相关接口")
@RequestMapping("/api/BOM")
@RestController
@CrossOrigin
public class BOMController {
    @Autowired
    private BOMLinkDelegator bomLinkDelegator;
    @Autowired
    private BOMUsesOccurrenceDelegator bomUsesOccurrenceDelegator;


    //创建BOM子项

    @PostMapping("/create")
    @CrossOrigin
    @ApiOperation("创建BOM")
    public Result createChildren(@RequestBody BOMDTO bomdto){
        BOMLinkCreateDTO bomLinkCreateDTO=new BOMLinkCreateDTO();
        BOMUsesOccurrenceCreateDTO bomUsesOccurrenceCreateDTO=new BOMUsesOccurrenceCreateDTO();
        ObjectReferenceParamDTO bomLink=new ObjectReferenceParamDTO();
        ObjectReferenceParamDTO source=new ObjectReferenceParamDTO();
        ObjectReferenceParamDTO target=new ObjectReferenceParamDTO();

        source.setId(bomdto.getSourceId());
        target.setId(bomdto.getTargetId());
        bomLinkCreateDTO.setSource(source);
        bomLinkCreateDTO.setTarget(target);
        bomLinkCreateDTO.setQuantity(bomdto.getQuantity());
        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.create(bomLinkCreateDTO);

        bomLink.setId(bomLinkViewDTO.getId());
        bomUsesOccurrenceCreateDTO.setBomLink(bomLink);
        bomUsesOccurrenceCreateDTO.setReferenceDesignator(bomdto.getReferenceDes());
        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO=bomUsesOccurrenceDelegator.create(bomUsesOccurrenceCreateDTO);

        //bomLinkViewDTO.setQuantity(bomdto.getQuantity());
        //bomUsesOccurrenceViewDTO.setReferenceDesignator(bomdto.getReferenceDes());
        return Result.success(bomUsesOccurrenceViewDTO);
    }

    //展示所有子项
    @PostMapping("/show/{pageSize}/{curPage}")
    @CrossOrigin
    @ApiOperation("展示所有子项")
    public Result showAllChildren(@RequestBody GenericLinkQueryDTO genericLinkQueryDTO, @PathVariable int pageSize, @PathVariable int curPage){
        RDMPageVO rdmPageVO=new RDMPageVO();
        rdmPageVO.setPageSize(pageSize);
        rdmPageVO.setCurPage(curPage);

        //role设置为source，输出target
        genericLinkQueryDTO.setRole("source");

        //List children=bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO);

        //role 设置为source，输出以其为source的BOMLink
        List<BOMLinkViewDTO> bomlinks=bomLinkDelegator.queryRelationship(genericLinkQueryDTO,rdmPageVO);
        //System.out.println(bomlinks.get(0));
        List<BOM> boms= new ArrayList<>();

     for(BOMLinkViewDTO bomLinkViewDTO:bomlinks){
         //查询条件
         QueryRequestVo queryRequestVo=new QueryRequestVo();
         queryRequestVo.addCondition("bomLink.id",ConditionType.EQUAL,bomLinkViewDTO.getId());
         List<BOMUsesOccurrenceViewDTO> bomUsesOccurrenceViewDTOS=bomUsesOccurrenceDelegator.find(queryRequestVo,rdmPageVO);
         //设置bom属性
         if(bomUsesOccurrenceViewDTOS!=null&& !bomUsesOccurrenceViewDTOS.isEmpty()){
             BOM bom=new BOM();
             bom.setQuantity(bomLinkViewDTO.getQuantity());
             bom.setReferenceDes(bomUsesOccurrenceViewDTOS.get(0).getReferenceDesignator());
             bom.setTargetId(bomLinkViewDTO.getTarget().getId());
             bom.setTargetName(bomLinkViewDTO.getTarget().getName());
             //加入boms
             boms.add(bom);
         }


     }
      //  System.out.println(boms.get(0));
        //System.out.println(bomlinks.size());

        return Result.success(boms);
    }

    //删除BOM子项
    @DeleteMapping("/delete")
    @CrossOrigin
    @ApiOperation("删除所选子项")
    public Result delete(@RequestBody PersistObjectIdModifierDTO persistObjectIdModifierDTO){

        //删除所有引用
        DeleteByConditionVo deleteByConditionVo=new DeleteByConditionVo();
        QueryRequestVo queryRequestVo=new QueryRequestVo();

        //查询name为bomLink.id
        queryRequestVo.addCondition("bomLink.id", ConditionType.EQUAL,persistObjectIdModifierDTO.getId());
        deleteByConditionVo.setCondition(queryRequestVo);
        bomUsesOccurrenceDelegator.delete(persistObjectIdModifierDTO);
        bomUsesOccurrenceDelegator.deleteByCondition(deleteByConditionVo);
        //persistObjectIdModifierDTO.getId();
        return Result.success(bomLinkDelegator.delete(persistObjectIdModifierDTO));
    }

}
