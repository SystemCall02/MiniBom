package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.dto.relation.*;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("创建BOMLink")
    @Operation(
            summary = "BOMLink"
    )
    public Result createBOMLink(@org.springframework.web.bind.annotation.RequestBody BOMLinkCreateDTO dto) {

        BOMLinkViewDTO viewDTO;
        viewDTO=bomLinkDelegator.create(dto);
        return Result.success(viewDTO);

    }


//修改数量
    @PutMapping("/update")
    @ApiOperation("修改BOMLink")
    public Result updateBOMLink(@RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO) {
        // 使用代理接口调用修改BOMLink的方法

        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.update(bomLinkUpdateDTO);
        // 构建成功响应
        return Result.success(bomLinkViewDTO);
    }

//删除
@DeleteMapping("/delete")
@ApiOperation("删除BOMLink")
public Result deleteBOMLink(@RequestBody PersistObjectIdModifierDTO persistObjectIdModifierDTO) {
    //PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
    //persistObjectIdModifierDTO.setId(id);
    bomLinkDelegator.delete(persistObjectIdModifierDTO);

    return Result.success();
}

//根据ID查询
@PostMapping("/getBOMLinks")
@ApiOperation("根据ID查询")
public Result getBOMLinks(@RequestBody PersistObjectIdDecryptDTO persistObjectIdDecryptDTO) {
     //   PersistObjectIdDecryptDTO persistObjectIdDecryptDTO=new PersistObjectIdDecryptDTO();
      //  persistObjectIdDecryptDTO.setId(id);

        BOMLinkViewDTO viewDTO=bomLinkDelegator.get(persistObjectIdDecryptDTO);
        return Result.success(viewDTO);
}

//获取父项
    @PostMapping ("/queryTarget/{pageSize}/{curPage}")
    @ApiOperation("获取父项")
    public Result queryTarget(@RequestBody GenericLinkTypeDTO genericLinkTypeDTO,@PathVariable int pageSize, @PathVariable int curPage) {
     //   bomLinkDelegator.queryTarget(genericLinkTypeDTO,rdmPageVO);
        RDMPageVO rdmPageVO=new RDMPageVO();
        rdmPageVO.setPageSize(pageSize);
        rdmPageVO.setCurPage(curPage);
        return Result.success(bomLinkDelegator.queryTarget(genericLinkTypeDTO,rdmPageVO));
    }
//获取父项或子项
    //传入Source：获取父项
    //传入Target：获取子项
    @PostMapping("/queryRelatedPart/{pageSize}/{curPage}")
    @ApiOperation("获取相关项")
    public Result queryRelatedPart(@RequestBody GenericLinkQueryDTO genericLinkQueryDTO,@PathVariable int pageSize, @PathVariable int curPage){
       RDMPageVO rdmPageVO=new RDMPageVO();
       rdmPageVO.setPageSize(pageSize);
       rdmPageVO.setCurPage(curPage);
     //  bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO);
       return Result.success(bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO));
    }


    @GetMapping("/{id}")
    @ApiOperation("传入partId获取子项信息")
    public Result queryChildrenInBomLink(@PathVariable Long id){
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("source.id", ConditionType.EQUAL,id);
        List<BOMLinkViewDTO> query = bomLinkDelegator.find(queryRequestVo,new RDMPageVO(1,50));
        return Result.success(query);
    }


}
