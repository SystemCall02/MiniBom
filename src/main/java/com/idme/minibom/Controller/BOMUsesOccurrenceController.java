package com.idme.minibom.Controller;


import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.san2.delegator.BOMUsesOccurrenceDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceUpdateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceViewDTO;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "BOMUsesOccurrence管理相关接口")
@RequestMapping("/api/BOMUsesOccurrence")
@RestController
@CrossOrigin

public class BOMUsesOccurrenceController {
    @Autowired
    private BOMUsesOccurrenceDelegator bomUsesOccurrenceDelegator;

    //创建
    @PostMapping("/create")
    @CrossOrigin
    @ApiOperation("创建BOMUsesOccurrence")
    public Result createBOMUsesOccurrence(@org.springframework.web.bind.annotation.RequestBody BOMUsesOccurrenceCreateDTO bomUsesOccurrenceCreateDTO) {
        System.out.println(bomUsesOccurrenceCreateDTO.getBomLink().getId());
        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO=bomUsesOccurrenceDelegator.create(bomUsesOccurrenceCreateDTO);
        return Result.success(bomUsesOccurrenceViewDTO);
    }
    //    "message": "[bomLink] can not be null;",

    //删除
    @DeleteMapping("/delete/{id}")
    @CrossOrigin
    @ApiOperation("删除BOMUsesOccurrence")
    public Result deleteBOMUserOccurrence(@PathVariable Long id) {
        PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
        persistObjectIdModifierDTO.setId(id);
        return Result.success(bomUsesOccurrenceDelegator.delete(persistObjectIdModifierDTO));
    }

    //修改
    @PutMapping("/update/{id}")
    @CrossOrigin
    @ApiOperation("修改BOMUsesOccurrence")
    public Result updateBOMLink(@RequestBody BOMUsesOccurrenceUpdateDTO bomUsesOccurrenceUpdateDTO) {
        // @RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO
        // 使用代理接口调用修改BOMLink的方法

        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO= bomUsesOccurrenceDelegator.update(bomUsesOccurrenceUpdateDTO);
        // 构建成功响应
        return Result.success(bomUsesOccurrenceViewDTO);
    }


}
