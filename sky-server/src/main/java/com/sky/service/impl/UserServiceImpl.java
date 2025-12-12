package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.constant.UrlConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author beaker
 * @Date 2025/12/12 20:15
 * @Description 用户管理服务层实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务, 获取当前用户openid
        Map<String, String> properties = new HashMap<>();
        properties.put(UrlConstant.APP_ID, weChatProperties.getAppid());
        properties.put(UrlConstant.SECRET, weChatProperties.getSecret());
        properties.put(UrlConstant.JS_CODE, userLoginDTO.getCode());
        properties.put(UrlConstant.GRANT_TYPE, UrlConstant.TYPE);
        String json = HttpClientUtil.doGet(UrlConstant.WX_LOGIN, properties);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");

        // 判断是否返回 openid
        if (openid == null || openid.isEmpty()) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断当前账户是否为新账户
        User user = userMapper.getByOpenid(openid);
        if (user != null) return user;

        // 新账户添加到表中
        user = User.builder()
                .openid(openid)
                .build();
        userMapper.insert(user);

        // 返回用户
        return user;
    }
}
