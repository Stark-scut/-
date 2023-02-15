package servlet;

import Pojo.Order;
import Pojo.OrderItem;
import Pojo.User;
import org.apache.ibatis.annotations.Insert;
import service.impl.OrderServiceImpl;
import service.impl.UserServiceImpl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/order/*")
public class OrderServlet extends baseServlet {
    private OrderServiceImpl orderService = new OrderServiceImpl();


    public void orderSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("user")!=null){
            request.getRequestDispatcher("/order_submit.jsp").forward(request,response);
        }else {
            request.setAttribute("failMsg","请登录后，再提交订单");
            request.getRequestDispatcher("/user_login.jsp").forward(request,response);
        }
    }

    public void orderConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = (Order) request.getSession().getAttribute("order");
        System.out.println("order");
//        System.out.println(order)
        Map<Integer, OrderItem> map = order.getItemMap();
        List<OrderItem> itemList = new ArrayList<>();
        String emailMsg = "";
        for(Map.Entry<Integer, OrderItem> entry: map.entrySet()){
            System.out.println("key="+entry.getKey()+" amount="+entry.getValue().getAmount());
            itemList.add(entry.getValue());
        }
        for (int i = 0; i < itemList.size(); i++) {
            OrderItem item = itemList.get(i);
            emailMsg = emailMsg + item.getGoodsName() + item.getAmount() +" x " + item.getPrice() + "￥ ";
            System.out.println(item.getGoodsName() + item.getAmount() +" x " + item.getPrice());
        }
        order.setItemList(itemList);
        System.out.println(order.getItemMap());
        String name = (String) request.getParameter("name");  // getParameter 获取post请求中的参数
        String phone = (String) request.getParameter("phone");
        String address = (String) request.getParameter("address");
        String payType = (String) request.getParameter("paytype");
        Integer paytype = Integer.parseInt(payType);
        System.out.println(name+" "+ phone+" "+address);
        order.setDatetime(new Date());
        order.setStatus(2);
        order.setName(name);
        order.setPhone(phone);
        order.setAddress(address);
        order.setPaytype(paytype);
        User user = (User) request.getSession().getAttribute("user");
        order.setUser(user);
        System.out.println(order);
        orderService.addOrder(order);
        request.getSession().removeAttribute("order");
        System.out.println("orderItemList");
        System.out.println(order.getItemList());

        // 发邮箱
        // 收件人电子邮箱
//        String to = "202066444372@mail.scut.edu.cn";
        String to = user.getEmail();
        // 发件人电子邮箱
        String from = "3482517952@qq.com";
        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("3482517952@qq.com", "mgpmmhlwdalmdbab"); //发件人邮件用户名、授权码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("KFC商店!");

            // 设置消息体
            message.setText("您的购买的"+emailMsg+"已发货，请注意查收");

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....from runoob.com");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }


        request.setAttribute("msg", "订单支付成功，请查收邮箱！");
        request.getRequestDispatcher("/order_success.jsp").forward(request, response);

    }


    public void orderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
        {
            response.sendRedirect("/index");
            return;
        }
        List<Order> orders = orderService.selectAll(user.getId());
        request.setAttribute("orderList", orders);
        request.getRequestDispatcher("/order_list.jsp").forward(request,response);

    }

}
