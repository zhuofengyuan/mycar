package com.qf.service;

import com.qf.dto.DataGridResult;
import com.qf.dto.QueryDTO;
import com.qf.pojo.OrderItem;
import com.qf.pojo.SysUser;

import java.util.List;

public interface OrderService {

    int insertByCartItem(List<Integer> ids, SysUser user);

    int insertByProduct(Integer ids, Integer qty, SysUser user);

    List<OrderItem> selectAll(SysUser user);

    DataGridResult findByPage(QueryDTO queryDTO);

    int updateStatus(List<Integer> id, Integer status);
}
