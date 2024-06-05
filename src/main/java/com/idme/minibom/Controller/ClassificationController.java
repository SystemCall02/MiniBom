package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeQueryViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.AttributeQueryDTO;
import com.idme.minibom.pojo.DTO.ClassificationQueryDTO;
import com.idme.minibom.pojo.VO.ClassificationQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "分类管理接口")
@RequestMapping("/idme/classification")
public class ClassificationController {

    @Autowired
    private ClassificationNodeDelegator classificationNodeDelegator;
    /**
     * 分页查询分类
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("分页查询分类节点")
    public Result pageQueryClass(@RequestBody ClassificationQueryDTO classificationQueryDTO){
        QueryRequestVo queryRequestVo = QueryVO(classificationQueryDTO);
        List<ClassificationNodeQueryViewDTO> queryResult =
                classificationNodeDelegator.query(queryRequestVo, new RDMPageVO(classificationQueryDTO.getCurPage()
                , classificationQueryDTO.getPageSize()));
        ClassificationQueryVO classificationQueryVO = new ClassificationQueryVO();
        classificationQueryVO.setResultList(queryResult);
        classificationQueryVO.setTotal(classificationNodeDelegator.count(queryRequestVo));

        return Result.success(classificationQueryVO);
    }

    /**
     * 统一封装查询VO
     * @param classificationQueryDTO
     * @return
     */
    private QueryRequestVo QueryVO(ClassificationQueryDTO classificationQueryDTO){
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        if(classificationQueryDTO.getName() == null || classificationQueryDTO.getName().isEmpty()){
            queryRequestVo.setIsNeedTotal(true);
        }else{
            char condition = classificationQueryDTO.getName().charAt(0);
            if(condition>65 && condition<90 || condition>97 && condition<122 || Character.isDigit(condition)){
                queryRequestVo.addCondition("businessCode", ConditionType.EQUAL, classificationQueryDTO.getName());
            }else{
                queryRequestVo.addCondition("name",ConditionType.LIKE, classificationQueryDTO.getName());
            }
        }


        return queryRequestVo;
    }
}
