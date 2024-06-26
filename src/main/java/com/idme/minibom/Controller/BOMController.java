package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.delegator.BOMUsesOccurrenceDelegator;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceUpdateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.BOMUsesOccurrenceViewDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkCreateDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkUpdateDTO;
import com.huawei.innovation.rdm.san2.dto.relation.BOMLinkViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.Class.BOM;
import com.idme.minibom.pojo.Class.BOMTreeNode;
import com.idme.minibom.pojo.DTO.BOMDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "BOM管理相关接口")
@RequestMapping("/api/BOM")
@RestController
@CrossOrigin
public class BOMController {
    @Autowired
    private BOMLinkDelegator bomLinkDelegator;
    @Autowired
    private BOMUsesOccurrenceDelegator bomUsesOccurrenceDelegator;
    @Autowired
    private PartDelegator partDelegator;

    //根据ID获取part
    //获取partMaster等相关信息
    public PartViewDTO getPart(Long Id){
        PersistObjectIdDecryptDTO persistObjectIdDecryptDTO = new PersistObjectIdDecryptDTO();
        persistObjectIdDecryptDTO.setId(Id);
        return partDelegator.get(persistObjectIdDecryptDTO);
    }



    //创建BOM子项
    //传入ID均为partID
    @PostMapping("/create")
    @ApiOperation("创建BOM子项")
    public Result createChildren(@RequestBody BOMDTO bomdto){
        BOMLinkCreateDTO bomLinkCreateDTO=new BOMLinkCreateDTO();
        BOMUsesOccurrenceCreateDTO bomUsesOccurrenceCreateDTO=new BOMUsesOccurrenceCreateDTO();
        ObjectReferenceParamDTO bomLink=new ObjectReferenceParamDTO();
        ObjectReferenceParamDTO source=new ObjectReferenceParamDTO();
        ObjectReferenceParamDTO target=new ObjectReferenceParamDTO();

        //传入参数
        source.setId(bomdto.getSourceId());
        if(getPart(bomdto.getTargetId())!=null) {
            //partId转为partMasterId
            target.setId(getPart(bomdto.getTargetId()).getMaster().getId());
            bomLinkCreateDTO.setSource(source);
            bomLinkCreateDTO.setTarget(target);
            bomLinkCreateDTO.setQuantity(bomdto.getQuantity());
            //创建bomLink
            BOMLinkViewDTO bomLinkViewDTO = bomLinkDelegator.create(bomLinkCreateDTO);

            bomLink.setId(bomLinkViewDTO.getId());
            bomUsesOccurrenceCreateDTO.setBomLink(bomLink);
            bomUsesOccurrenceCreateDTO.setReferenceDesignator(bomdto.getReferenceDes());
            //创建bomUseOccurrence
            BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO = bomUsesOccurrenceDelegator.create(bomUsesOccurrenceCreateDTO);
            //bomLinkViewDTO.setQuantity(bomdto.getQuantity());
            //bomUsesOccurrenceViewDTO.setReferenceDesignator(bomdto.getReferenceDes());
            return Result.success(bomUsesOccurrenceViewDTO);
        }
        else {
            return Result.success("target is not exist");
        }
    }

    //批量创建子项
    @PostMapping("/batchCreate")
    @ApiOperation("批量创建BOM子项")
    public Result batchCreateChildren(@RequestBody List<BOMDTO> bomdtoList){
        BOMLinkCreateDTO bomLinkCreateDTO=new BOMLinkCreateDTO();
        BOMUsesOccurrenceCreateDTO bomUsesOccurrenceCreateDTO=new BOMUsesOccurrenceCreateDTO();
        ObjectReferenceParamDTO bomLink=new ObjectReferenceParamDTO();
        ObjectReferenceParamDTO source=new ObjectReferenceParamDTO();
        ObjectReferenceParamDTO target=new ObjectReferenceParamDTO();
//列表
        List<BOMLinkCreateDTO> bomLinkCreateDTOList=new ArrayList<>();
        List<BOMUsesOccurrenceCreateDTO> bomUsesOccurrenceCreateDTOList=new ArrayList<>();

        if(bomdtoList!=null&&!bomdtoList.isEmpty()) {
            //批量传入参数
            for (BOMDTO bomdto : bomdtoList) {
                source.setId(bomdto.getSourceId());
                //partId转为partMasterId
                target.setId(getPart(bomdto.getTargetId()).getMaster().getId());
                bomLinkCreateDTO.setSource(source);
                bomLinkCreateDTO.setTarget(target);
                bomLinkCreateDTO.setQuantity(bomdto.getQuantity());
                //加入链表
                bomLinkCreateDTOList.add(bomLinkCreateDTO);

            }
            //批量创建bomLink
            List<BOMLinkViewDTO> bomLinkViewDTOList = bomLinkDelegator.batchCreate(bomLinkCreateDTOList);

            // BOMLinkViewDTO bomLinkViewDTO = bomLinkDelegator.create(bomLinkCreateDTO);
            for (BOMLinkViewDTO bomLinkViewDTO : bomLinkViewDTOList) {
                bomLink.setId(bomLinkViewDTO.getId());
                bomUsesOccurrenceCreateDTO.setBomLink(bomLink);
                bomUsesOccurrenceCreateDTO.setReferenceDesignator(bomdtoList.get(0).getReferenceDes());
                //加入链表
                bomUsesOccurrenceCreateDTOList.add(bomUsesOccurrenceCreateDTO);
            }
            //批量创建bomUseOccurrence
            //BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO = bomUsesOccurrenceDelegator.create(bomUsesOccurrenceCreateDTO);
            List<BOMUsesOccurrenceViewDTO> bomUsesOccurrenceViewDTOList = bomUsesOccurrenceDelegator.batchCreate(bomUsesOccurrenceCreateDTOList);


            return Result.success(bomUsesOccurrenceViewDTOList);
        }else {
            return Result.success("target is not exist");
        }
    }




    //展示所有子项
    //可增加几个属性或分开
    //入参为partId
    @PostMapping("/show/{pageSize}/{curPage}")
    @ApiOperation("展示所有子项")
    public Result showAllChildren(@RequestBody GenericLinkQueryDTO genericLinkQueryDTO, @PathVariable int pageSize, @PathVariable int curPage){
        RDMPageVO rdmPageVO=new RDMPageVO();
        rdmPageVO.setPageSize(pageSize);
        rdmPageVO.setCurPage(curPage);

        //将partId转为partMasterId
        Long sourceId=genericLinkQueryDTO.getObjectId();
        if(getPart(sourceId)!=null) {
            genericLinkQueryDTO.setObjectId(getPart(sourceId).getMaster().getId());
            //role设置为target，输出以其为父项的BOMLink
            genericLinkQueryDTO.setRole("target");

            //List children=bomLinkDelegator.queryRelatedObjects(genericLinkQueryDTO,rdmPageVO);

            List<BOMLinkViewDTO> bomlinks = bomLinkDelegator.queryRelationship(genericLinkQueryDTO, rdmPageVO);
            List<BOM> boms = new ArrayList<>();

            //错误处理：检测子项是否存在
            if(bomlinks!=null&&!bomlinks.isEmpty()) {
                for (BOMLinkViewDTO bomLinkViewDTO : bomlinks) {
                    //查询BOMLink对应BOMUseOccurrence
                    QueryRequestVo queryRequestVo = new QueryRequestVo();
                    queryRequestVo.addCondition("bomLink.id", ConditionType.EQUAL, bomLinkViewDTO.getId());
                    List<BOMUsesOccurrenceViewDTO> bomUsesOccurrenceViewDTOS = bomUsesOccurrenceDelegator.find(queryRequestVo, rdmPageVO);
                    //设置bom属性
                    if (bomUsesOccurrenceViewDTOS != null && !bomUsesOccurrenceViewDTOS.isEmpty()) {
                        BOM bom = new BOM();
                        bom.setQuantity(bomLinkViewDTO.getQuantity());
                        bom.setReferenceDes(bomUsesOccurrenceViewDTOS.get(0).getReferenceDesignator());
                        bom.setSourceId(bomLinkViewDTO.getSource().getId());
                        bom.setSourceName(bomLinkViewDTO.getSource().getName());
                        //加入boms
                        boms.add(bom);
                    }


                }
                //  System.out.println(boms.get(0));
                //System.out.println(bomlinks.size());
                return Result.success(boms);
            }
            else {
                return Result.success("children is not exist");
            }

        }
        else {
            return Result.success("part is not exist");
        }
    }

    //删除所选BOM子项
    //删除所有BOMUseOccurrence引用并删除子项
    //传入ID为bomlinkId
    @DeleteMapping("/delete")
    @ApiOperation("删除所选子项")
    public Result delete(@RequestBody PersistObjectIdModifierDTO persistObjectIdModifierDTO){

        DeleteByConditionVo deleteByConditionVo=new DeleteByConditionVo();
        QueryRequestVo queryRequestVo=new QueryRequestVo();

        //查询name为bomLink.id
        queryRequestVo.addCondition("bomLink.id", ConditionType.EQUAL,persistObjectIdModifierDTO.getId());
        deleteByConditionVo.setCondition(queryRequestVo);
        //删除所有引用
        bomUsesOccurrenceDelegator.deleteByCondition(deleteByConditionVo);
        //persistObjectIdModifierDTO.getId();
        return Result.success(bomLinkDelegator.delete(persistObjectIdModifierDTO));
    }

    //修改数量
    @PutMapping("/updateQuantity")
    @ApiOperation("修改BOMLink")
    public Result updateBOMLink(@RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO) {
        // 使用代理接口调用修改BOMLink的方法

        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.update(bomLinkUpdateDTO);
        // 构建成功响应
        return Result.success(bomLinkViewDTO);
    }

    //修改位号
    @PutMapping("/updateDes")
    @ApiOperation("修改BOMUsesOccurrence")
    public Result updateBOMUsesOccurrence(@RequestBody BOMUsesOccurrenceUpdateDTO bomUsesOccurrenceUpdateDTO) {

        BOMUsesOccurrenceViewDTO bomUsesOccurrenceViewDTO= bomUsesOccurrenceDelegator.update(bomUsesOccurrenceUpdateDTO);
        // 构建成功响应
        return Result.success(bomUsesOccurrenceViewDTO);
    }

    //查看BOM
    /*
    *逐一获取每个Part的子项
    * */
    public BOMTreeNode addChildren( BOMTreeNode root){
        GenericLinkQueryDTO genericLinkQueryDTO=new GenericLinkQueryDTO();
        genericLinkQueryDTO.setObjectId(root.getPartMasterId());
        genericLinkQueryDTO.setRole("target");
        RDMPageVO rdmPageVO=new RDMPageVO();
        rdmPageVO.setPageSize(100);
        rdmPageVO.setCurPage(1);
        //获取以其为父节点的BOMLink
        if(genericLinkQueryDTO.getObjectId()!=null) {
            List<BOMLinkViewDTO> bomLinkViewDTOList = bomLinkDelegator.queryRelationship(genericLinkQueryDTO, rdmPageVO);
            if (bomLinkViewDTOList != null && !bomLinkViewDTOList.isEmpty()) {
                for (BOMLinkViewDTO bomLinkViewDTO : bomLinkViewDTOList) {

                    //获取子节点MasterID，name,Number
                    BOMTreeNode node = new BOMTreeNode(bomLinkViewDTO.getSource().getMaster().getId(),bomLinkViewDTO.getSource().getMaster().getName(),bomLinkViewDTO.getSource().getMaster().getNumber());
                    root.addChild(addChildren(node));
                   // root.addChild(node);
                }
            }
        }

       // bomLinkViewDTO.getSource().getMaster();
       // BOMTreeNode node=new BOMTreeNode(partMasterId,)
        return root;

    }
    //创建树
    //传入ID为partID
    @PostMapping("/createTree")
    @ApiOperation("创建BOMTree")
    public Result createTree(@RequestBody PersistObjectIdModifierDTO persistObjectIdModifierDTO){

        BOMTreeNode root=new BOMTreeNode(persistObjectIdModifierDTO.getId(),getPart(persistObjectIdModifierDTO.getId()).getMaster().getName(),getPart(persistObjectIdModifierDTO.getId()).getMaster().getNumber());
        BOMTreeNode bomTree=addChildren(root);
      //  System.out.println(root.getPartMasterId());
        return Result.success(bomTree);
    }


}
