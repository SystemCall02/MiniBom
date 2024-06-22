package com.idme.minibom.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.enums.JoinerType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.delegate.common.XdmDelegateConsts;
import com.huawei.innovation.rdm.delegate.service.XdmTokenService;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeRelatedByTypeDefinitionDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeQueryViewDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeUpdateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeViewDTO;
import com.idme.minibom.Constant.APIConstant;
import com.idme.minibom.Constant.ExceptionConstant;
import com.idme.minibom.Exception.ClassificationNodeDeleteNotAllowedException;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.AddClassificationNodeAttrDTO;
import com.idme.minibom.pojo.DTO.ClassificationQueryDTO;
import com.idme.minibom.pojo.DTO.DeleteClassificationNodeAttrDTO;
import com.idme.minibom.pojo.VO.ClassificationQueryVO;
import com.idme.minibom.pojo.VO.ClassificationTreeVO;
import com.idme.minibom.pojo.VO.QueryClassificationNodeAttrVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@Api(tags = "分类管理接口")
@RequestMapping("/idme/classification")
@CrossOrigin
public class ClassificationController {

    @Autowired
    private ClassificationNodeDelegator classificationNodeDelegator;

    @Autowired
    private XdmTokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PartDelegator partDelegator;




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
        //TODO 暂时有父节点ID为空的错误出现，等重构IDME中的分类树状结构即可在前端必须指定值防止为空
        ClassificationNodeViewDTO classificationNodeViewDTO = classificationNodeDelegator.create(createDTO);
        return Result.success(classificationNodeViewDTO.getId());
    }


    @GetMapping("/getAttr/{linkId}")
    @ApiOperation("查询分类的属性")
    public Result getNodeAttr(@PathVariable Long linkId){
        String url = APIConstant.ENDPOINT+"/ClassificationNode/getCategoryNodeInfo?linkId={linkId}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(XdmDelegateConsts.X_AUTH_TOKEN,tokenService.getToken());
        HashMap<String,Long> params = new HashMap<>();
        params.put("linkId",linkId);
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        JSONObject body = restTemplate.exchange(url, HttpMethod.GET, request, JSONObject.class, params).getBody();
        List<QueryClassificationNodeAttrVO> result = new ArrayList<>();
        JSONArray attrs = body.getJSONArray("data").getJSONObject(0).getJSONArray("attrs");
        for (int i=0;i<attrs.size();i++){
            QueryClassificationNodeAttrVO queryClassificationNodeAttrVO = new QueryClassificationNodeAttrVO();
            queryClassificationNodeAttrVO.setId(attrs.getJSONObject(i).getLong("id"));
            queryClassificationNodeAttrVO.setName(attrs.getJSONObject(i).getString("name"));
            queryClassificationNodeAttrVO.setNameEn(attrs.getJSONObject(i).getString("nameEn"));
            queryClassificationNodeAttrVO.setDescription(attrs.getJSONObject(i).getString("description"));
            queryClassificationNodeAttrVO.setDescriptionEn(attrs.getJSONObject(i).getString("descriptionEn"));
            result.add(queryClassificationNodeAttrVO);
        }

        return Result.success(result);
    }

    @PostMapping("/update")
    @ApiOperation("更新分类信息")
    public Result updateClassification(@RequestBody ClassificationNodeUpdateDTO classificationNodeUpdateDTO){
        classificationNodeDelegator.update(classificationNodeUpdateDTO);
        return Result.success();
    }



    @PostMapping("/addAttr")
    @ApiOperation("增加分类关联属性")
    public Result addClassificationNodeAttr(@RequestBody AddClassificationNodeAttrDTO addClassificationNodeAttrDTO){
        String url = APIConstant.ENDPOINT+"/ClassificationNode/attribute/add";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(XdmDelegateConsts.X_AUTH_TOKEN,tokenService.getToken());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("attrIds",addClassificationNodeAttrDTO.getAttrIds());
        jsonObject.put("holderId",addClassificationNodeAttrDTO.getHolderId());
        JSONObject params = new JSONObject();
        params.put("params",jsonObject);
        HttpEntity<JSONObject> request = new HttpEntity<>(params,headers);
        restTemplate.postForEntity(url,request, JSONObject.class);

        return Result.success();

    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除分类节点")
    public Result deleteClassificationNode(@PathVariable Long id){
        if(!isClassificationNodeReferredNUll(id)){
            throw new ClassificationNodeDeleteNotAllowedException(ExceptionConstant.Class_DELETE_NOT_ALLOWED);
        }
        PersistObjectIdModifierDTO persistObjectIdModifierDTO = new PersistObjectIdModifierDTO();
        persistObjectIdModifierDTO.setId(id);
        classificationNodeDelegator.delete(persistObjectIdModifierDTO);

        return Result.success();
    }


    private boolean isClassificationNodeReferredNUll(Long id){
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("extAttrs.Classification",ConditionType.EQUAL,id);
        return partDelegator.count(queryRequestVo) == 0;
    }

    /**
     * 删除分类属性，需注意传入的是方法《查询分类属性》中代表分类和属性之间关联的ID，并非属性本身的ID
     * @param deleteClassificationNodeAttrDTO
     * @return
     */
    @DeleteMapping("/deleteAttr")
    @ApiOperation("删除分类属性")
    public Result deleteClassificationNodeAttr(@RequestBody DeleteClassificationNodeAttrDTO deleteClassificationNodeAttrDTO){
        String url = APIConstant.ENDPOINT+"/ClassificationNode/attribute/remove";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(XdmDelegateConsts.X_AUTH_TOKEN,tokenService.getToken());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("linkIds",deleteClassificationNodeAttrDTO.getLinkIds());
        JSONObject params = new JSONObject();
        params.put("params",jsonObject);
        HttpEntity<JSONObject> request = new HttpEntity<>(params,headers);
        restTemplate.postForEntity(url,request, JSONObject.class);

        return Result.success();
    }










}
