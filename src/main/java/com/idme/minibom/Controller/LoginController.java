package com.idme.minibom.Controller;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.UserDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;

import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.LoginDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "登录相关接口")
@RequestMapping
@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private UserDelegator userDelegator;

    /**
     * 查询接口，Delegator代理类直接返回ResponseBody中的data部分。
     *
     * @return 数据实例列表
     */
    @ApiOperation("查询用户名是否存在")
    @PostMapping("/find")
    public List<UserViewDTO> find(String name) {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("name", ConditionType.EQUAL,name);
        return userDelegator.find(queryRequestVo, new RDMPageVO(1, 10));
    }

    /**
     * 登录接口
     * @param loginDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        List<UserViewDTO> userViewDTOS = find(loginDTO.getName());
        UserViewDTO responseResult = new UserViewDTO();
        if(userViewDTOS==null||userViewDTOS.isEmpty()){
            //Exception
            return Result.error(404,"用户不存在");
        }
        UserViewDTO queryResult = userViewDTOS.get(0);
        //password加密 DigestUtils.md5DigestAsHex()
        if(!loginDTO.getPassword().equals(queryResult.getPassword())){
            //Exception
            return Result.error(400,"用户账号或密码错误");
        }

        responseResult.setName(loginDTO.getName());
        responseResult.setId(queryResult.getId());

        return Result.success("登录成功",responseResult);

    }

}
