package com.idme.minibom.Controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.delegate.service.XdmTokenService;
import com.huawei.innovation.rdm.san2.delegator.BOMLinkDelegator;
import com.huawei.innovation.rdm.san2.dto.relation.*;
import com.idme.minibom.Request.BOMLinkRequest;
import com.idme.minibom.Result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@Api(tags = "BOMLink管理相关接口")
@RequestMapping("/api/BOMLink")
@RestController
@CrossOrigin
public class BOMLinkController {
    @Autowired
    private BOMLinkDelegator bomLinkDelegator;

    @Autowired
    private  RestTemplate restTemplate;

    //获取token
    @Autowired
    private XdmTokenService tokenService;

    //IBOMLinkService service;


//创建
    @PostMapping("/create")
    @CrossOrigin
    @ApiOperation("创建BOMLink")
    @Operation(
            summary = "BOMLink"
    )
    public Result createBOMLink(@org.springframework.web.bind.annotation.RequestBody BOMLinkRequest dto) {

        //创建api地址
        String remoteApiUrl = "https://dme.cn-north-4.huaweicloud.com/rdm_83f47cac09064530837465ac4dd55c20_app/publicservices/api/BOMLink/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //设置令牌
        headers.set("x-auth-token",tokenService.getToken());


     /*   HashMap<String,Object> params = new HashMap<>();
        params.put("creator",dto.getCreator());
        params.put("id",dto.getId());
        params.put("source",dto.getSource());
        params.put("target",dto.getTarget());
*/


        //System.out.println(params);
        HttpEntity<String> requestEntity = new HttpEntity<>(dto.toString(), headers);
        //System.out.println(requestEntity.getBody());
       // System.out.println(requestEntity.getBody());

        ResponseEntity<String> response = restTemplate.exchange(
                remoteApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class,
                new ParameterizedTypeReference<String>() {}
        );
     //   System.out.println(response);


       String viewDTO = response.getBody();
      //  System.out.println(viewDTO);
        return Result.success(viewDTO);

       /* BOMLinkViewDTO viewDTO;
        viewDTO=bomLinkDelegator.create(dto);
        return Result.success(viewDTO);*/

    }


//修改
    @PutMapping("/update/{id}")
    @CrossOrigin
    @ApiOperation("修改BOMLink")
    public Result updateBOMLink(@RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO) {
       // @RequestBody BOMLinkUpdateDTO bomLinkUpdateDTO
        // 使用代理接口调用修改BOMLink的方法

        BOMLinkViewDTO bomLinkViewDTO=bomLinkDelegator.update(bomLinkUpdateDTO);
        // 构建成功响应
        return Result.success(bomLinkViewDTO);
    }

//删除
@DeleteMapping("/delete/{id}")
@CrossOrigin
@ApiOperation("删除BOMLink")
public Result deleteBOMLink(@PathVariable Long id) {
    PersistObjectIdModifierDTO persistObjectIdModifierDTO=new PersistObjectIdModifierDTO();
    persistObjectIdModifierDTO.setId(id);
    bomLinkDelegator.delete(persistObjectIdModifierDTO);

    return Result.success();
}
/*
//根据ID查询
@GetMapping("/getBOMLinks}")
public Result getBOMLinks(@org.springframework.web.bind.annotation.RequestBody Long id) {
    QueryRequestVo queryRequestVo=new QueryRequestVo();
    RDMPageVO rdmPageVO=new RDMPageVO();
        List<BOMLinkQueryViewDTO> bomLinks = bomLinkDelegator.query(queryRequestVo,rdmPageVO);
        return new ResponseEntity<>(bomLinks, HttpStatus.OK);
}
*/

}
