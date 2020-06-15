package com.qf.dao;

import com.qf.pojo.CarParamsRoot;
import com.qf.pojo.CarParamsRootExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarParamsRootMapper {
    int countByExample(CarParamsRootExample example);

    int deleteByExample(CarParamsRootExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CarParamsRoot record);

    int insertSelective(CarParamsRoot record);

    List<CarParamsRoot> selectByExample(CarParamsRootExample example);

    CarParamsRoot selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CarParamsRoot record, @Param("example") CarParamsRootExample example);

    int updateByExample(@Param("record") CarParamsRoot record, @Param("example") CarParamsRootExample example);

    int updateByPrimaryKeySelective(CarParamsRoot record);

    int updateByPrimaryKey(CarParamsRoot record);
}