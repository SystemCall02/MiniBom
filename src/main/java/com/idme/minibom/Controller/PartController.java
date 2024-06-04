package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.extattrmgmt.dto.EXAValueParamDTO;
import com.huawei.innovation.rdm.coresdk.extattrmgmt.dto.EXAValueViewDTO;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/idme")
@Api("part相关接口")
public class PartController {

    @Autowired
    private ClassificationNodeDelegator classificationNodeDelegator;

    @Autowired
    private PartDelegator partDelegator;

    @ApiOperation("创建part")
    @PostMapping("/createPart")
    public PartViewDTO createPart(){
        PartCreateDTO partCreateDTO = new PartCreateDTO();
        EXAValueParamDTO exaValueParamDTO = new EXAValueParamDTO();
      /*  exaValueParamDTO.setName("Classification");
        exaValueParamDTO.setValue("3_服务器");*/
        List<EXAValueParamDTO> list = new ArrayList<>();
        list.add(exaValueParamDTO);
        partCreateDTO.setExtAttrs(list);
        return partDelegator.create(partCreateDTO);
    }

    @PostMapping("/getPart")
    public PartViewDTO getPart(){
        PersistObjectIdDecryptDTO persistObjectIdDecryptDTO = new PersistObjectIdDecryptDTO();
        persistObjectIdDecryptDTO.setId(637655581850611712L);

        PartViewDTO result = partDelegator.get(persistObjectIdDecryptDTO);

        System.out.println(result.toString());
        return result;
    }


}
