package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author beaker
 * @Date 2025/12/11 16:39
 * @Description TODO
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据dishId查询id
     * @param id
     * @return
     */
    Long getSetmealIdByDishId(Long id);

    /**
     * 新增套餐菜品
     * @param setmealDish
     */
    void insert(SetmealDish setmealDish);

    /**
     * 删除套餐关联的菜品
     * @param id
     */
    void delete(Long id);
}
