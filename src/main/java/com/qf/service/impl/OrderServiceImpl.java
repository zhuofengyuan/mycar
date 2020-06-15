package com.qf.service.impl;

import com.qf.dao.CartItemMapper;
import com.qf.dao.CartMapper;
import com.qf.dao.OrderItemMapper;
import com.qf.dao.OrderMapper;
import com.qf.pojo.*;
import com.qf.service.CartService;
import com.qf.service.OrderService;
import com.qf.service.ProductCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductCarService productCarService;
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    CartItemMapper cartItemMapper;

    @Override
    public int insertByCartItem(List<Integer> ids, SysUser user) {
        List<CartItem> c = cartService.selectByCartItemIds(ids);
        Order o = new Order();
        o.setUserId(Integer.valueOf(user.getUserId().toString()));
        o.setAddress(user.getAddress());
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        int r = orderMapper.insertSelective(o);
        dataSourceTransactionManager.commit(transactionStatus);

        for(CartItem item : c){
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodsTotalPrice(item.getGoodsTotalPrice());
            orderItem.setGoodsPrice(item.getGoodsPrice());
            orderItem.setQty(item.getQty());
            orderItem.setGoodsId(item.getGoodsId());
            orderItem.setGoodsName(item.getGoodsName());
            orderItem.setCreateTime(new Date());
            orderItem.setOrderId(o.getId());
            orderItemMapper.insert(orderItem);
        }
        return r;
    }

    @Override
    public int insertByProduct(Integer ids, Integer qty, SysUser user) {
        ProductWithBLOBs item = productCarService.findByParentID(ids);

        Order o = new Order();
        o.setUserId(Integer.valueOf(user.getUserId().toString()));
        o.setAddress(user.getAddress());
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        int r = orderMapper.insertSelective(o);
        dataSourceTransactionManager.commit(transactionStatus);

        OrderItem orderItem = new OrderItem();
        orderItem.setGoodsTotalPrice(qty * item.getPrice());
        orderItem.setGoodsPrice(item.getPrice());
        orderItem.setQty(qty);
        orderItem.setGoodsId(item.getId());
        orderItem.setGoodsName(item.getName());
        orderItem.setCreateTime(new Date());
        orderItem.setOrderId(o.getId());
        orderItemMapper.insert(orderItem);
        return r;
    }

    @Override
    public List<CartItem> selectAll(SysUser user) {
        CartExample e = new CartExample();
        e.createCriteria().andUseridEqualTo(Integer.parseInt(user.getUserId().toString()));
        Cart c = this.cartMapper.selectByExample(e).get(0);
        CartItemExample ex = new CartItemExample();
        ex.createCriteria().andCartIdEqualTo(c.getId());
        return this.cartItemMapper.selectByExample(ex);
    }
}
