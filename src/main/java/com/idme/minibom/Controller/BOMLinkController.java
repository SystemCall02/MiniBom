package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.delegate.service.XdmTokenService;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceCreateDTO;
import com.huawei.innovation.rdm.san2.dto.relation.*;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Api(tags = "BOMLink管理相关接口")
@RequestMapping("/api/BOMLink")
@RestController
@CrossOrigin
public class BOMLinkController {
    @Autowired
    private BOMLinkDelegator bomLinkDelegator;

    //@Autowired
  //  private  RestTemplate restTemplate;

    //获取token
   // @Autowired
    //private XdmTokenService tokenService;



//创建
    //json格式与postman调用url不同
    @PostMapping("/create")
    @CrossOrigin
    @ApiOperation("创建BOMLink")
    @Operation(
            summary = "BOMLink"
    )
    public Result createBOMLink(@org.springframework.web.bind.annotation.RequestBody BOMLinkCreateDTO dto) {

        BOMLinkViewDTO viewDTO;
        viewDTO=bomLinkDelegator.create(dto);
        return Result.success(viewDTO);

    }


//修改
    @PutMapping("/update")
    @CrossOrigin
    @ApiOperation("修改BOMLink")
    public Result updateBOMLink(@RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO) {
        // 使用代理接口调用修改BOMLink的方法

        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.update(bomLinkUpdateDTO);
        // 构建成功响应
        return Result.success(bomLinkViewDTO);
    }

//删除
@DeleteMapping("/delete")
@CrossOrigin
@ApiOperation("删除BOMLink")
public Result deleteBOMLink(@RequestBody PersistObjectIdModifierDTO persistObjectIdModifierDTO) {
    //PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
    //persistObjectIdModifierDTO.setId(id);
    bomLinkDelegator.delete(persistObjectIdModifierDTO);

    return Result.success();
}

//根据ID查询
@GetMapping("/getBOMLinks")
@CrossOrigin
@ApiOperation("根据ID查询")
public Result getBOMLinks(@org.springframework.web.bind.annotation.RequestBody  PersistObjectIdDecryptDTO persistObjectIdDecryptDTO) {
     //   PersistObjectIdDecryptDTO persistObjectIdDecryptDTO=new PersistObjectIdDecryptDTO();
      //  persistObjectIdDecryptDTO.setId(id);
        BOMLinkViewDTO viewDTO=bomLinkDelegator.get(persistObjectIdDecryptDTO);
        return Result.success(viewDTO);
}

//获取子项
    @PostMapping ("/queryTarget/{pageSize}/{curPage}")
    @CrossOrigin
    @ApiOperation("获取子项")
    public Result queryTarget(@RequestBody GenericLinkTypeDTO genericLinkTypeDTO,@PathVariable int pageSize, @PathVariable int curPage) {
     //   bomLinkDelegator.queryTarget(genericLinkTypeDTO,rdmPageVO);
        RDMPageVO rdmPageVO=new RDMPageVO();
        rdmPageVO.setPageSize(pageSize);
        rdmPageVO.setCurPage(curPage);
        return Result.success(bomLinkDelegator.queryTarget(genericLinkTypeDTO,rdmPageVO));
    }
//获取父项或子项
    //传入Source：获取子项
    //传入Target：获取父项
    @PostMapping("/queryRelatedPart/{pageSize}/{curPage}")
    @CrossOrigin
    @ApiOperation("获取相关项")
    public Result queryRelatedPart(@RequestBody GenericLinkQueryDTO genericLinkQueryDTO,@PathVariable int pageSize, @PathVariable int curPage){
       RDMPageVO rdmPageVO=new RDMPageVO();
       rdmPageVO.setPageSize(pageSize);
       rdmPageVO.setCurPage(curPage);
     //  bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO);
       return Result.success(bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO));
    }

 /*   //ToDo 展示所有子项
    public Result showAllChilds(ObjectReferenceParamDTO source,ObjectReferenceParamDTO target){
        BOMLinkCreateDTO bomLinkCreateDTO=new BOMLinkCreateDTO();
        BOMUsesOccurrenceCreateDTO bomUsesOccurrenceCreateDTO=new BOMUsesOccurrenceCreateDTO();
     //   ObjectReferenceParamDTO objectReferenceParamDTO=new ObjectReferenceParamDTO();
        bomLinkCreateDTO.setSource(source);
        bomLinkCreateDTO.setTarget(target);
        bomLinkDelegator.create(bomLinkCreateDTO);



        return Result.success();
    }
*/

/*@GetMapping("/queryBOMLinks")
@CrossOrigin
@ApiOperation("根据条件分页查询")
    public Result queryBOMLinks(@RequestBody QueryRequestVo queryRequestVo, RDMPageVO rdmPageVO) {
        List<BOMLinkQueryViewDTO> queryViewDTO=bomLinkDelegator.query(queryRequestVo,rdmPageVO);
        return Result.success(queryViewDTO);
}*/

}
