package com.qf.service;

import com.qf.dto.DataGridResult;
import com.qf.dto.QueryDTO;
import com.qf.pojo.CarParamsRoot;

public interface CarParamRootService {

    public int addCarParams(CarParamsRoot carParams);
    public void delCarParams(Integer id);
    public int updateCarParams(CarParamsRoot carParams);
    public CarParamsRoot findById(Integer id);
    public DataGridResult findByPage(QueryDTO queryDTO);



}
