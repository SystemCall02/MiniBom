package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.xdm.delegator.EXADefinitionDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionQueryViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.AttrCreateDTO;
import com.idme.minibom.pojo.DTO.AttributeQueryDTO;
import com.idme.minibom.pojo.VO.AttributeQueryVO;
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
@RequestMapping("/idme/attribute")
public class AttributeController {

    @Autowired
    private EXADefinitionDelegator exaDefinitionDelegator;

    /**
     * 属性分页查询
     * @param attributeQueryDTO
     * @return
     */
    @ApiOperation("分页查询属性")
    @PostMapping("/pageQueryAttr")
    public Result pageQueryAttribute(@RequestBody AttributeQueryDTO attributeQueryDTO){
        QueryRequestVo queryRequestVo = QueryVO(attributeQueryDTO);
        AttributeQueryVO attributeQueryVO = new AttributeQueryVO();
        List<EXADefinitionQueryViewDTO> queryResult = exaDefinitionDelegator
                .query(queryRequestVo, new RDMPageVO(attributeQueryDTO.getCurPage(), attributeQueryDTO.getPageSize()));
        long total = exaDefinitionDelegator.count(queryRequestVo);

        attributeQueryVO.setResultList(queryResult);
        attributeQueryVO.setTotal(total);

        return Result.success(attributeQueryVO);
    }

    /**
     * 统一封装查询VO
     * @param attributeQueryDTO
     * @return
     */
    private QueryRequestVo QueryVO(AttributeQueryDTO attributeQueryDTO){
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        if(attributeQueryDTO.getName() == null || attributeQueryDTO.getName().isEmpty()){
            queryRequestVo.setIsNeedTotal(true);
        }else{
            char condition = attributeQueryDTO.getName().charAt(0);
            if(condition>65 && condition<90 || condition>97 && condition<122){
                queryRequestVo.addCondition("nameEn", ConditionType.LIKE, attributeQueryDTO.getName());
            }else{
                queryRequestVo.addCondition("name",ConditionType.LIKE, attributeQueryDTO.getName());
            }
        }


        return queryRequestVo;
    }

    /**
     * 创建属性
     */
    @PostMapping("/createAttr")
    @ApiOperation("创建属性")
    public Result createAttr(@RequestBody AttrCreateDTO attrCreateDTO){
        EXADefinitionCreateDTO createDTO = new EXADefinitionCreateDTO();
        createDTO.setName(attrCreateDTO.getName());
        createDTO.setNameEn(attrCreateDTO.getNameEn());
        createDTO.setType(attrCreateDTO.getType());
        createDTO.setDisableFlag(!attrCreateDTO.getEnableFlag());
        createDTO.setDescription(attrCreateDTO.getDescription());
        createDTO.setDescriptionEn(attrCreateDTO.getDescriptionEn());
        //Constraint格式：json字符串
        //Constraint文本类型至少需要指定的信息：
        // "{\"length\":100,\"notnull\":false,\"variable\":true,\"stockInDB\":true,\"secretLevel\":\"internal\",\"caseMode\":\"DEFAULT\",\"multiValue\":false}"
        //其他类型可先通过IDME人工创建，利用Postman Query查看形式，再编码调用
        //TODO 是否允许用户指定约束信息看时间进度
        String dataType = attrCreateDTO.getType();
        if(dataType.equals("STRING")){
            createDTO.setConstraint("{\"length\":200,\"notnull\":false,\"variable\":true,\"stockInDB\":true,\"secretLevel\":\"internal\",\"caseMode\":\"DEFAULT\",\"multiValue\":false}");
        }else if(dataType.equals("DECIMAL")){
            //待定
        }

        exaDefinitionDelegator.create(createDTO);

        return Result.success();
    }

}
