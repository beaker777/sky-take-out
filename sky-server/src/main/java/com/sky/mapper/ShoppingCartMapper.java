package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author beaker
 * @Date 2025/12/15 21:28
 * @Description 购物车管理持久层
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据 id 将数量 +1
     * @param shoppingCart
     */
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 删除购物车数据
     * @param shoppingCart
     */
    void delete(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param currentId
     */
    void cleanByUserId(Long currentId);
}
