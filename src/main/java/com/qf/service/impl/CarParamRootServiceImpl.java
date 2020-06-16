package com.qf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.dao.CarParamsRootMapper;
import com.qf.dto.DataGridResult;
import com.qf.dto.QueryDTO;
import com.qf.pojo.CarParamsRoot;
import com.qf.pojo.CarParamsRootExample;
import com.qf.service.CarParamRootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CarParamRootServiceImpl implements CarParamRootService {

    @Autowired
    private CarParamsRootMapper carParamsMapper;

    @Override
    public int addCarParams(CarParamsRoot carParams) {
        return carParamsMapper.insertSelective(carParams);
    }

    @Override
    public void delCarParams(Integer id) {
        carParamsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateCarParams(CarParamsRoot carParams) {
        return carParamsMapper.updateByPrimaryKeySelective(carParams);
    }

    @Override
    public CarParamsRoot findById(Integer id) {
        return carParamsMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(),queryDTO.getLimit());
        CarParamsRootExample example = new CarParamsRootExample();
        String sort = queryDTO.getSort();
        if(!StringUtils.isEmpty(sort)){
            example.setOrderByClause("id");
        }
        String search = queryDTO.getSearch();
        if(!StringUtils.isEmpty(search)){
            example.createCriteria().andParamNameLike("%" + search + "%");
        }
        List<CarParamsRoot> carParams = carParamsMapper.selectByExample(example);
        PageInfo<CarParamsRoot> info = new PageInfo<>(carParams);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total,carParams);
        return result;
    }
}
