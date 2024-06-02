package com.idme.minibom.Controller;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.UserDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;

import com.idme.minibom.pojo.DTO.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DelegatorController
 *
 * @since 2024-04-11
 */
@RequestMapping("/delegator")
@RestController
public class LoginController {

/*    @Autowired
    private PersistableModelDelegator persistableModelDelegator;*/

    @Autowired
    private UserDelegator userDelegator;

    /**
     * 查询接口，Delegator代理类直接返回ResponseBody中的data部分。
     *
     * @return 数据实例列表
     */
    //@RequestMapping(value = "/find", method = RequestMethod.POST)
    @PostMapping("/find")
    public List<UserViewDTO> find(String name) {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("name", ConditionType.EQUAL,name);
        return userDelegator.find(queryRequestVo, new RDMPageVO(1, 10));
    }

/*    @PostMapping("/part")
    public PartViewDTO getPart(){
        PartCreateDTO partCreateDTO = new PartCreateDTO();
        PartMasterCreateDTO partMasterCreateDTO = new PartMasterCreateDTO();
        partMasterCreateDTO.setId(5L);
        PartBranchCreateDTO partBranchCreateDTO = new PartBranchCreateDTO();
        partBranchCreateDTO.setId(5L);
        partCreateDTO.setMaster(partMasterCreateDTO);
        partCreateDTO.setBranch(partBranchCreateDTO);
        return partDelegator.create(partCreateDTO);
    }*/

    @PostMapping("/login")
    public UserViewDTO login(@RequestBody LoginDTO loginDTO){
        List<UserViewDTO> userViewDTOS = find(loginDTO.getName());
        UserViewDTO responseResult = new UserViewDTO();
        if(userViewDTOS.isEmpty()){
            //Exception
        }
        UserViewDTO queryResult = userViewDTOS.get(0);
        //password加密 DigestUtils.md5DigestAsHex()
        if(loginDTO.getPassword().equals(queryResult.getPassword())){
            //Exception
        }

        responseResult.setName(loginDTO.getName());
        responseResult.setId(queryResult.getId());

        return responseResult;

    }

}
