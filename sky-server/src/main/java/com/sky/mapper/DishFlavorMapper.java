package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author beaker
 * @Date 2025/12/11 14:21
 * @Description 菜品口味持久层
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 插入菜品口味数据
     * @param flavor
     */
    public void insert(DishFlavor flavor);

    /**
     * 根据菜品id删除数据
     * @param id
     */
    void deleteByDishId(Long id);

    /**
     * 根据dishId获取口味数据
     * @param id
     * @return
     */
    List<DishFlavor> getByDishId(Long id);
}
