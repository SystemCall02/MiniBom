package com.idme.minibom.Controller;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.UserDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.*;

import com.idme.minibom.Constant.ExceptionConstant;
import com.idme.minibom.Constant.LoginConstant;
import com.idme.minibom.Exception.PasswordErrorException;
import com.idme.minibom.Exception.UserNotFountException;
import com.idme.minibom.Exception.UsernameExistException;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.LoginDTO;
import com.idme.minibom.utils.AESUtil;
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
    public Result find(String name) {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("name", ConditionType.EQUAL,name);
        Long count = userDelegator.count(queryRequestVo);
        return Result.success(count);
    }

    /**
     * 登录接口
     * @param loginDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) throws Exception {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("name", ConditionType.EQUAL,loginDTO.getName());
        List<UserViewDTO> userViewDTOS = userDelegator.find(queryRequestVo, new RDMPageVO(1, 10));

        UserViewDTO responseResult = new UserViewDTO();
        if(userViewDTOS==null||userViewDTOS.isEmpty()){
            throw new UserNotFountException(ExceptionConstant.USER_NOT_FOUNT);
        }
        UserViewDTO queryResult = userViewDTOS.get(0);
        String password = AESUtil.encrypt(loginDTO.getPassword());
        if(password.equals(queryResult.getPassword())){
           throw new PasswordErrorException(ExceptionConstant.PASSWORD_ERROR);
        }

//        responseResult.setName(loginDTO.getName());
//        responseResult.setId(queryResult.getId());
        //为了获取用户更多信息 只把隐私信息设空
        responseResult = queryResult;
        responseResult.setPassword("");
        return Result.success(LoginConstant.LOGIN_SUCCESS,responseResult);
    }


    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result userRegister(@RequestBody UserCreateDTO userCreateDTO) throws Exception {
        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("name", ConditionType.EQUAL,userCreateDTO.getName());
        Long count = userDelegator.count(queryRequestVo);

        if(count != 0){
            throw new UsernameExistException(ExceptionConstant.USERNAME_EXIST);
        }

        userCreateDTO.setPassword(AESUtil.encrypt(userCreateDTO.getPassword()));
        userDelegator.create(userCreateDTO);

        return Result.success();
    }
}
