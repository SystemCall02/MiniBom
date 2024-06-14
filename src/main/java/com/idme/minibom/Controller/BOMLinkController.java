package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.delegate.service.XdmTokenService;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.dto.relation.*;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@Api(tags = "BOMLink管理相关接口")
@RequestMapping("/api/BOMLink")
@RestController
@CrossOrigin
public class BOMLinkController {
    @Autowired
    private BOMLinkDelegator bomLinkDelegator;

    @Autowired
    private  RestTemplate restTemplate;

    //获取token
    @Autowired
    private XdmTokenService tokenService;

    //IBOMLinkService service;


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
    bomLinkDelegator.delete(persistObjectIdModifierDTO);

    return Result.success();
}

//根据ID查询
@GetMapping("/getBOMLinks")
public Result getBOMLinks(@org.springframework.web.bind.annotation.RequestBody  PersistObjectIdDecryptDTO persistObjectIdDecryptDTO) {
     //   PersistObjectIdDecryptDTO persistObjectIdDecryptDTO=new PersistObjectIdDecryptDTO();
      //  persistObjectIdDecryptDTO.setId(id);
        BOMLinkViewDTO viewDTO=bomLinkDelegator.get(persistObjectIdDecryptDTO);
        return Result.success(viewDTO);
}


}
