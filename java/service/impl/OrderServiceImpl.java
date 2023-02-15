package service.impl;

import Pojo.Order;
import Pojo.OrderItem;
import Pojo.Page;
import Pojo.User;
import mapper.GoodsMapper;
import mapper.OrderMapper;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.OrderService;
import service.UserService;
import utils.SqlSessionFactoryUtils;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();



    @Override
    public void addOrder(Order order) {    //  在数据库中插入订单和订单项
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);

        // 插入订单
        mapper.addOrder(order);
        sqlSession.commit();
        Integer id = mapper.selectLastId();   // 获取orderID
        order.setId(id);
        // 插入订单项
        for(OrderItem item: order.getItemMap().values()){
            mapper.addOrderItem(item, id);
        }
        sqlSession.commit();
        sqlSession.close();
        return;
    }

    @Override
    public List<Order> selectAll(int userId) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);

        List<Order> orders = mapper.selectOrdersByUserId(userId);
        for(Order order : orders){
            List<OrderItem> itemList = mapper.selectOrderItemByOrderId(order.getId());
            order.setItemList(itemList);
        }

        sqlSession.close();
        return orders;
    }

    @Override
    public int getOrderCount(int status) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        int count = 0;
        if(status==0){  // 获取order总数
            count = mapper.getOrderCount();
        }else{
            count = mapper.getOrderCountByStatus(status);
        }
        sqlSession.close();
        return count;
    }

    @Override
    public List<Order> getOrderListByStatus(int status, int pageNumber, int pageSize) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        int begin = (pageNumber-1)*pageSize;
        List<Order> list = null;
        if(status==0){
            try {
                list = mapper.getAllOrderList(begin, pageSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                list = mapper.getOrderListByStatus(status, begin, pageSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        sqlSession.close();
        return list;
    }

    @Override
    public String getUserNameByOrderId(int orderId) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        String username = mapper.getUserNameByOrderId(orderId);
        sqlSession.close();
        return username;
    }

    @Override
    public void updateStatus(int id, int status) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        mapper.updateStatus(id, status);
        sqlSession.commit();
        sqlSession.close();
        return;
    }

    @Override
    public void delete(int id) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        mapper.delete(id);
        sqlSession.commit();
        sqlSession.close();
        return;
    }

    @Override
    public Page getOrderPage(int status, int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);

        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 10;
        int totalCount = 0;
        try {
            totalCount = getOrderCount(status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.SetPageSizeAndTotalCount(pageSize, totalCount);
        List list= null;
        list = getOrderListByStatus(status,pageNumber,pageSize);
        for(Order order : (List<Order>)list){
            List<OrderItem> itemList = mapper.selectOrderItemByOrderId(order.getId());
            order.setItemList(itemList);
            order.setUsername(getUserNameByOrderId(order.getId()));
        }
        p.setList(list);
        sqlSession.close();
        return p;
    }



}

