package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.GenericLinkQueryDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.bean.relation.BOMLink;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.delegator.BOMUsesOccurrenceDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceViewDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkCreateDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.BOMDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    @PostMapping("/show")
    @CrossOrigin
    @ApiOperation("展示所有子项")
    public Result showAllChilds(@RequestBody GenericLinkQueryDTO genericLinkQueryDTO, @PathVariable int pageSize, @PathVariable int curPage){
        RDMPageVO rdmPageVO=new RDMPageVO();
        rdmPageVO.setPageSize(pageSize);
        rdmPageVO.setCurPage(curPage);
        List children=bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO);
        List<BOMLinkViewDTO> bomlinks=bomLinkDelegator.queryRelationship(genericLinkQueryDTO,rdmPageVO);

        return Result.success();
    }

}
