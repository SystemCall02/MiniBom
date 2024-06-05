package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.dto.relation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.huawei.innovation.rdm.coresdk.basic.event.ServiceEventListener.LOGGER;

@Api(tags = "BOMLink管理相关接口")
@RequestMapping("/api/BOMLink")
@RestController
public class BOMLinkController {
    @Autowired
    private  BOMLinkDelegator bomLinkDelegator;

    public BOMLinkController(BOMLinkDelegator bomLinkDelegator) {
        this.bomLinkDelegator = bomLinkDelegator;
    }
//创建
    @PostMapping("/create")
    @ApiOperation("创建BOMLink")
    public ResponseEntity<BOMLinkViewDTO> createBOMLink(@RequestBody BOMLinkCreateDTO bomLinkCreateDTO) {
        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.create(bomLinkCreateDTO);

    return new ResponseEntity<>(bomLinkViewDTO, HttpStatus.CREATED);
}

//修改
    @PutMapping("/update/{id}")
    @ApiOperation("修改BOMLink")
    public ResponseEntity<BOMLinkViewDTO> updateBOMLink(@PathVariable Long id, @RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO) {
        // 使用代理接口调用修改BOMLink的方法
        BOMLinkViewDTO updatedBOMLink=bomLinkDelegator.update(bomLinkUpdateDTO);
        // 构建成功响应
        return new ResponseEntity<>(updatedBOMLink, HttpStatus.OK);
    }
//删除
@DeleteMapping("/delete/{id}")
@ApiOperation("删除BOMLink")
public ResponseEntity<Void> deleteBOMLink(@PathVariable Long id) {
    PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
    persistObjectIdModifierDTO.setId(id);
    bomLinkDelegator.delete(persistObjectIdModifierDTO);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
