package com.qf.service;

import com.qf.pojo.SysUser;

import java.util.List;

public interface OrderService {

    int insertByCartItem(List<Integer> ids, SysUser user);

    int insertByProduct(Integer ids, Integer qty, SysUser user);

    int selectAll(SysUser user);
}