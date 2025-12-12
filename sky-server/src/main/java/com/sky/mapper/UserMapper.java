package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author beaker
 * @Date 2025/12/12 20:16
 * @Description 用户管理持久层
 */
@Mapper
public interface UserMapper {

    /**
     * 根据 openid 查询账户
     * @param openid
     * @return
     */
    User getByOpenid(String openid);

    /**
     * 新增用户
     * @param user
     */
    void insert(User user);
}
