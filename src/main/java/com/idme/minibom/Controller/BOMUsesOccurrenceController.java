package com.idme.minibom.Controller;


import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.san2.delegator.BOMUsesOccurrenceDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceUpdateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceViewDTO;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("创建BOMUsesOccurrence")
    public Result createBOMUsesOccurrence(@org.springframework.web.bind.annotation.RequestBody BOMUsesOccurrenceCreateDTO bomUsesOccurrenceCreateDTO) {
     //   System.out.println(bomUsesOccurrenceCreateDTO.getBomLink().getId());
        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO=bomUsesOccurrenceDelegator.create(bomUsesOccurrenceCreateDTO);
        return Result.success(bomUsesOccurrenceViewDTO);
    }
    //    "message": "[bomLink] can not be null;",

    //删除
    @DeleteMapping("/delete")
    @ApiOperation("删除BOMUsesOccurrence")
    public Result deleteBOMUserOccurrence(@org.springframework.web.bind.annotation.RequestBody PersistObjectIdModifierDTO persistObjectIdModifierDTO) {
      //  PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
       // persistObjectIdModifierDTO.setId(id);
        return Result.success(bomUsesOccurrenceDelegator.delete(persistObjectIdModifierDTO));
    }

    //修改
    @PutMapping("/update")
    @ApiOperation("修改BOMUsesOccurrence")
    public Result updateBOMUsesOccurrence(@RequestBody BOMUsesOccurrenceUpdateDTO bomUsesOccurrenceUpdateDTO) {

        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO= bomUsesOccurrenceDelegator.update(bomUsesOccurrenceUpdateDTO);
        // 构建成功响应
        return Result.success(bomUsesOccurrenceViewDTO);
    }

    //根据ID获取BOMUsesOccurrence
    @PostMapping("/get")
    @ApiOperation("获取BOMUsesOccurrence")
    public Result getBOMUsesOccurrence(@RequestBody PersistObjectIdDecryptDTO persistObjectIdDecryptDTO) {

        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO=bomUsesOccurrenceDelegator.get(persistObjectIdDecryptDTO);
        return Result.success(bomUsesOccurrenceViewDTO);
    }

}
