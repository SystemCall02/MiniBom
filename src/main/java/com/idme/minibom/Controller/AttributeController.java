package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.xdm.delegator.EXADefinitionDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionQueryViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.AttributeDTO;
import com.idme.minibom.pojo.VO.AttributeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "属性相关接口")
@RequestMapping("/idme")
public class AttributeController {

    @Autowired
    private EXADefinitionDelegator exaDefinitionDelegator;

    /**
     * 属性分页查询
     * @param attributeDTO
     * @return
     */
    @ApiOperation("分页查询属性")
    @PostMapping("/pageQueryAttr")
    public Result pageQueryAttribute(@RequestBody AttributeDTO attributeDTO){
        QueryRequestVo queryRequestVo = QueryVO(attributeDTO);
        AttributeVO attributeVO = new AttributeVO();
        List<EXADefinitionQueryViewDTO> queryResult = exaDefinitionDelegator
                .query(queryRequestVo, new RDMPageVO(attributeDTO.getCurPage(), attributeDTO.getPageSize()));
        long total = exaDefinitionDelegator.count(queryRequestVo);

        attributeVO.setResultList(queryResult);
        attributeVO.setTotal(total);

        return Result.success(attributeVO);
    }

    /**
     * 统一封装查询VO
     * @param attributeDTO
     * @return
     */
    private QueryRequestVo QueryVO(AttributeDTO attributeDTO){
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        char condition = attributeDTO.getName().charAt(0);
        if(condition>65 && condition<90 || condition>97 && condition<122){
            queryRequestVo.addCondition("nameEn", ConditionType.LIKE,attributeDTO.getName());
        }else{
            queryRequestVo.addCondition("name",ConditionType.LIKE,attributeDTO.getName());
        }

        return queryRequestVo;
    }

}
