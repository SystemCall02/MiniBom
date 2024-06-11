package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.dto.relation.*;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "BOMLink管理相关接口")
@RequestMapping("/api/BOMLink")
@RestController
@CrossOrigin
public class BOMLinkController {
    @Autowired
    private BOMLinkDelegator bomLinkDelegator;

//创建
    @PostMapping("/create{id}")
    @CrossOrigin
    @ApiOperation("创建BOMLink")
    public Result createBOMLink(@RequestBody BOMLinkCreateDTO bomLinkCreateDTO) {
         BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.create(bomLinkCreateDTO);
         return Result.success(bomLinkViewDTO);
    }


//修改
    @PutMapping("/update/{id}")
    @CrossOrigin
    @ApiOperation("修改BOMLink")
    public Result updateBOMLink(@RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO) {
       // @RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO
        // 使用代理接口调用修改BOMLink的方法

        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.update(bomLinkUpdateDTO);
        // 构建成功响应
        return Result.success(bomLinkViewDTO);
    }

//删除
@DeleteMapping("/delete/{id}")
@CrossOrigin
@ApiOperation("删除BOMLink")
public Result deleteBOMLink(@PathVariable Long id) {
    PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
    persistObjectIdModifierDTO.setId(id);
    //bomLinkDelegator.delete(persistObjectIdModifierDTO);
    bomLinkDelegator.delete(persistObjectIdModifierDTO);

    return Result.success();
}
//
/*@GetMapping("/getBOMLinks/{id}")
public ResponseEntity<List<BOMLinkQueryViewDTO>> getBOMLinks(@PathVariable Long id) {
    QueryRequestVo queryRequestVo=new QueryRequestVo();
    RDMPageVO rdmPageVO=new RDMPageVO();
    //*



    //*
        List<BOMLinkQueryViewDTO> bomLinks = bomLinkDelegator.query(queryRequestVo,rdmPageVO);
        return new ResponseEntity<>(bomLinks, HttpStatus.OK);
}*/

}
