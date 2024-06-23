package com.idme.minibom.Controller;

import com.alibaba.fastjson.JSONObject;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.enums.JoinerType;
import com.huawei.innovation.rdm.coresdk.basic.vo.*;
import com.huawei.innovation.rdm.delegate.common.XdmDelegateConsts;
import com.huawei.innovation.rdm.delegate.service.XdmTokenService;
import com.huawei.innovation.rdm.xdm.delegator.*;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionQueryViewDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionUpdateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionViewDTO;
import com.idme.minibom.Config.RestTemplateConfig;
import com.idme.minibom.Constant.APIConstant;
import com.idme.minibom.Constant.AttributeConstraintConstant;
import com.idme.minibom.Constant.ExceptionConstant;
import com.idme.minibom.Exception.AttributeNotAllowedDeleteException;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.AttrCreateDTO;
import com.idme.minibom.pojo.DTO.AttrUpdateDTO;
import com.idme.minibom.pojo.DTO.AttributeQueryDTO;
import com.idme.minibom.pojo.VO.AttributeQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "属性相关接口")
@RequestMapping("/idme/attribute")
@CrossOrigin
public class AttributeController {

    @Autowired
    private EXADefinitionDelegator exaDefinitionDelegator;

    @Autowired
    private XdmTokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;


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
        List<EXADefinitionViewDTO> queryResult = exaDefinitionDelegator
                .find(queryRequestVo, new RDMPageVO(attributeQueryDTO.getCurPage(), attributeQueryDTO.getPageSize()));
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
        if (attributeQueryDTO.getName() == null || attributeQueryDTO.getName().isEmpty()) {
            queryRequestVo.setIsNeedTotal(true);
        } else {
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setJoiner(JoinerType.OR.getJoiner());
            queryCondition.addCondition("nameEn", ConditionType.LIKE, attributeQueryDTO.getName());
            queryCondition.addCondition("name", ConditionType.LIKE, attributeQueryDTO.getName());
            queryRequestVo.setFilter(queryCondition);
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
        //约束信息是否用户自定义根据时间进度
        String dataType = attrCreateDTO.getType();
        if(dataType.equals(AttributeConstraintConstant.STRING_TYPE)){
            createDTO.setConstraint(AttributeConstraintConstant.STRING_CONSTRAINT);
        }else if(dataType.equals(AttributeConstraintConstant.DECIMAL_TYPE)){
            createDTO.setConstraint(AttributeConstraintConstant.DECIMAL_CONSTRAINT);
        }

        exaDefinitionDelegator.create(createDTO);

        return Result.success();
    }

    /**
     * 更新属性信息，仅中英文描述
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("更新属性")
    private Result updateAttr(@RequestBody AttrUpdateDTO attrUpdateDTO){
        EXADefinitionUpdateDTO exaDefinitionUpdateDTO = new EXADefinitionUpdateDTO();
        exaDefinitionUpdateDTO.setId(attrUpdateDTO.getId());
        exaDefinitionUpdateDTO.setType(attrUpdateDTO.getType());
        exaDefinitionUpdateDTO.setDescription(attrUpdateDTO.getDescription());
        exaDefinitionUpdateDTO.setDescriptionEn(attrUpdateDTO.getDescriptionEn());
        if (attrUpdateDTO.getType().equals(AttributeConstraintConstant.STRING_TYPE)){
            exaDefinitionUpdateDTO.setConstraint(AttributeConstraintConstant.STRING_CONSTRAINT);
        }else{
            exaDefinitionUpdateDTO.setConstraint(AttributeConstraintConstant.DECIMAL_CONSTRAINT);
        }
        exaDefinitionDelegator.update(exaDefinitionUpdateDTO);
        return Result.success();
    }

    /**
     * 删除属性，需注意属性不能被任何分类引用
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除属性")
    public Result deleteAttr(@PathVariable Long id){
        if(!isAttrReferenceNull(id)){
            throw new AttributeNotAllowedDeleteException(ExceptionConstant.ATTR_DELETE_NOT_ALLOWED);
        }

        PersistObjectIdModifierDTO persistObjectIdModifierDTO = new PersistObjectIdModifierDTO();
        persistObjectIdModifierDTO.setId(id);
        exaDefinitionDelegator.delete(persistObjectIdModifierDTO);
        return Result.success();

    }

    //@GetMapping("/reference")
    private boolean isAttrReferenceNull(Long id){
        String url = APIConstant.ENDPOINT+"//EXADefinition/queryExadefinitionNodeRefered/10/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(XdmDelegateConsts.X_AUTH_TOKEN,tokenService.getToken());
        //QueryRequestVo queryRequestVo = new QueryRequestVo();
/*        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",637593447506649088L);*/
       // RDMParamVO
        HashMap<String,Long> params = new HashMap<>();
        params.put("id",id);
        HttpEntity<HashMap<String,Long>> request = new HttpEntity<>(params,headers);

        RDMResultVO body = restTemplate.postForEntity(url, request, RDMResultVO.class).getBody();

        return body.getData().isEmpty();

    }

    @GetMapping("/reference/{id}")
    public Result getAttrReferenced(@PathVariable Long id){
        String url = APIConstant.ENDPOINT+"//EXADefinition/queryExadefinitionNodeRefered/50/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(XdmDelegateConsts.X_AUTH_TOKEN,tokenService.getToken());
        HashMap<String,Long> params = new HashMap<>();
        params.put("id",id);
        HttpEntity<HashMap<String,Long>> request = new HttpEntity<>(params,headers);

        RDMResultVO body = restTemplate.postForEntity(url, request, RDMResultVO.class).getBody();

        return Result.success(body.getData());

    }


}
