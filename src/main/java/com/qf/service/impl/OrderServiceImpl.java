package com.qf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.dao.CartItemMapper;
import com.qf.dao.CartMapper;
import com.qf.dao.OrderItemMapper;
import com.qf.dao.OrderMapper;
import com.qf.dto.DataGridResult;
import com.qf.dto.QueryDTO;
import com.qf.pojo.*;
import com.qf.service.CartService;
import com.qf.service.OrderService;
import com.qf.service.ProductCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
            orderItem.setImg(item.getImg());
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
        orderItem.setImg(item.getImg());
        orderItemMapper.insert(orderItem);
        return r;
    }

    @Override
    public List<OrderItem> selectAll(SysUser user) {
        OrderExample e = new OrderExample();
        e.createCriteria().andUserIdEqualTo(Integer.parseInt(user.getUserId().toString()));
        List<Order> c = this.orderMapper.selectByExample(e);
        List<Integer> ids = c.stream().map(Order::getId).collect(Collectors.toList());
        OrderItemExample ex = new OrderItemExample();
        ex.createCriteria().andOrderIdIn(ids);
        return this.orderItemMapper.selectByExample(ex);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(),queryDTO.getLimit());
        OrderItemExample example = new OrderItemExample();
        String sort = queryDTO.getSort();
        if(!StringUtils.isEmpty(sort)){
            example.setOrderByClause("id");
        }
        String search = queryDTO.getSearch();
        if(!StringUtils.isEmpty(search)){
            example.createCriteria().andGoodsNameLike("%" + search + "%");
        }
        List<OrderItem> articles = orderItemMapper.selectByExample(example);
        articles.forEach(e -> e.setUsername(this.orderMapper.selectNameByOrderId(e.getOrderId())));
        PageInfo<OrderItem> info = new PageInfo<>(articles);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total,articles);
        return result;
    }

    @Override
    public int updateStatus(List<Integer> id, Integer status) {
        OrderItem record = new OrderItem();
        record.setStatus(status);

        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andIdIn(id);
        return this.orderItemMapper.updateByExampleSelective(record, example);
    }
}
