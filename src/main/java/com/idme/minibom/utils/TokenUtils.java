package com.idme.minibom.utils;


import com.idme.minibom.pojo.DTO.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token工具类
 */
public class TokenUtils {

    RedisTemplate redisTemplate = new RedisTemplate();

    /**
     * 根据用户id生成token，并存入redis
     * @param user
     * @return
     */
    //生成token
    public String generateToken(LoginDTO user) {
        //先对用户进行验证
        if (user==null|| StringUtils.isEmpty(user.getName())){
            return null;
        }
        String token = "user:"+user.getName()+":"+UUID.randomUUID().toString();
        //存入redis 并设置过期时间
        redisTemplate.opsForValue().set(token,user,6, TimeUnit.HOURS);
        return token;
    }

    //验证token
    public boolean validateToken(String token) {
        if (token==null||StringUtils.isEmpty(token)){
            return false;
        }
        LoginDTO user = (LoginDTO) redisTemplate.opsForValue().get(token);
        if (user==null){
            return false;
        }
        return true;
    }

    //删除token
    public boolean deleteToken(String token) {
        if (token==null||StringUtils.isEmpty(token)){
            return false;
        }
        return redisTemplate.delete(token);
    }

}
