package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author beaker
 * @Date 2025/12/11 20:19
 * @Description 套餐管理服务层接口实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Override
    public void save(SetmealDTO setmealDTO) {
        // 插入套餐
        Setmeal setmeal = Setmeal.builder()
                .categoryId(setmealDTO.getCategoryId())
                .description(setmealDTO.getDescription())
                .image(setmealDTO.getImage())
                .name(setmealDTO.getName())
                .price(setmealDTO.getPrice())
                .build();
        setmealMapper.save(setmeal);

        // 获取套餐id
        Long setmealId = setmeal.getId();

        // 插入套餐菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.insert(setmealDish);
        }
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 删除套餐
     *
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);

            // 判断是否正在销售
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }

            // 删除菜品
            setmealMapper.delete(id);

            // 删除菜品关联的套餐
            setmealDishMapper.delete(id);
        }
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        // 获取套餐
        Setmeal setmeal = setmealMapper.getById(id);

        // 获取套餐关联菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 编辑套餐
     *
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = Setmeal.builder()
                .id(setmealDTO.getId())
                .categoryId(setmealDTO.getCategoryId())
                .description(setmealDTO.getDescription())
                .image(setmealDTO.getImage())
                .name(setmealDTO.getName())
                .price(setmealDTO.getPrice())
                .status(setmealDTO.getStatus())
                .build();
        setmealMapper.update(setmeal);

        // 删除套餐关联菜品
        setmealDishMapper.delete(setmealDTO.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.insert(setmealDish);
        }
    }

    /**
     * 启用或禁用套餐
     *
     * @param status
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();

        setmealMapper.update(setmeal);
    }
}
