package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.enums.JoinerType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeRelatedByTypeDefinitionDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeQueryViewDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.ClassificationQueryDTO;
import com.idme.minibom.pojo.VO.ClassificationQueryVO;
import com.idme.minibom.pojo.VO.ClassificationTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "分类管理接口")
@RequestMapping("/idme/classification")
@CrossOrigin
public class ClassificationController {

    @Autowired
    private ClassificationNodeDelegator classificationNodeDelegator;





    @Autowired
    private  ClassificationNodeRelatedByTypeDefinitionDelegator classificationNodeRelatedByTypeDefinitionDelegator;

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
        if (classificationQueryDTO.getName() == null || classificationQueryDTO.getName().isEmpty()) {
            queryRequestVo.setIsNeedTotal(true);
        } else {
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setJoiner(JoinerType.OR.getJoiner());
            queryCondition.addCondition("businessCode", ConditionType.LIKE, classificationQueryDTO.getName());
            queryCondition.addCondition("name", ConditionType.LIKE, classificationQueryDTO.getName());
            queryRequestVo.setFilter(queryCondition);
        }
        return queryRequestVo;
    }

    /**
     * 分类树状结构
     * @return
     */
    @GetMapping("/tree")
    @ApiOperation("返回分类树状结构")
    public Result ClassificationTree(){
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        Long count = classificationNodeDelegator.count(queryRequestVo);
        List<ClassificationNodeViewDTO> fingAllList =
                classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, count.intValue()));

        List<ClassificationTreeVO> result = buildTree(fingAllList);

        return Result.success(result);
    }

    /**
     * 构建分类树
     * @param findAllList
     * @return
     */
    private List<ClassificationTreeVO> buildTree(List<ClassificationNodeViewDTO> findAllList){
        List<ClassificationTreeVO> resultList = new ArrayList<>();
        for(ClassificationNodeViewDTO classificationNodeViewDTO: findAllList){
            //将父节点为空的节点（根节点）加入到结果集中
            if(classificationNodeViewDTO.getParentNode() == null){
                ClassificationTreeVO classificationTreeVO = castToClassificationTreeVO(classificationNodeViewDTO);
                resultList.add(classificationTreeVO);
            }
        }

        for (ClassificationTreeVO parent : resultList) {
            recursionFindChildren(parent,findAllList);
        }
        return resultList;
    }

    /**
     * 递归寻找子节点
     * @param parent
     * @param findAllList
     */
    private void recursionFindChildren(ClassificationTreeVO parent, List<ClassificationNodeViewDTO> findAllList) {
        List<ClassificationTreeVO> children = new ArrayList<>();

        for (ClassificationNodeViewDTO classificationNodeViewDTO : findAllList) {
            if(classificationNodeViewDTO.getParentNode() == null)
                continue;

            if(classificationNodeViewDTO.getParentNode().getId().equals(parent.getId())){
                ClassificationTreeVO child = castToClassificationTreeVO(classificationNodeViewDTO);
                recursionFindChildren(child,findAllList);
                children.add(child);
            }
        }

        parent.setChildren(children);
    }

    /**
     * 将ClassificationNodeViewDTO转成TreeVO对象
     * @param classificationNodeViewDTO
     * @return
     */
    private ClassificationTreeVO castToClassificationTreeVO(ClassificationNodeViewDTO classificationNodeViewDTO){
        return ClassificationTreeVO.builder()
                .id(classificationNodeViewDTO.getId())
                .businessCode(classificationNodeViewDTO.getBusinessCode())
                .enableFlag(!classificationNodeViewDTO.getDisableFlag())
                .name(classificationNodeViewDTO.getName())
                .nameEn(classificationNodeViewDTO.getNameEn())
                .build();
    }

    /**
     * 创建分类
     */
    @PostMapping("/create")
    @ApiOperation("创建分类")
    public Result createClassification(@RequestBody ClassificationNodeCreateDTO createDTO){
        //TODO 暂时不清楚如何为分类指定属性
        //TODO 暂时有父节点ID为空的错误出现，等重构IDME中的分类树状结构即可在前端必须指定值防止为空
        classificationNodeDelegator.create(createDTO);
        return Result.success();
    }




}
