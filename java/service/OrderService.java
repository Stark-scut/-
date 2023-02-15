package service;

import Pojo.Order;
import Pojo.Page;
import Pojo.User;

import java.util.List;

public interface OrderService {


    void addOrder(Order order);
    List<Order> selectAll(int userId);
    // 根据status获取对应order数量
    int getOrderCount(int status);

    List<Order> getOrderListByStatus(int status, int pageNumber, int pageSize);
    Page getOrderPage(int status, int pageNumber);

    // 根据orderId获取用户名
    String getUserNameByOrderId(int orderId);

    void updateStatus(int id, int status);

    void delete(int id);

}
