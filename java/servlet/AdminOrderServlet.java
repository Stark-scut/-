package servlet;

import Pojo.Order;
import Pojo.OrderItem;
import Pojo.Page;
import Pojo.User;
import service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/adminOrder/*")
public class AdminOrderServlet extends baseServlet {
    private OrderServiceImpl orderService = new OrderServiceImpl();

    public void orderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        if(request.getParameter("status") != null) {
            status=Integer.parseInt(request.getParameter("status") ) ;
        }
        request.setAttribute("status", status);
        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        if(pageNumber<=0)
            pageNumber=1;
        Page p = orderService.getOrderPage(status,pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);

        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = orderService.getOrderPage(status,pageNumber);
            }
        }

        request.setAttribute("p", p);
        request.getRequestDispatcher("/admin/order_list.jsp").forward(request, response);
    }

    public void orderStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        orderService.updateStatus(id, status);
        response.sendRedirect("http://localhost:8080/KFC/adminOrder/orderList");
    }

    public void orderDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        orderService.delete(id);
        response.sendRedirect("http://localhost:8080/KFC/adminOrder/orderList");
    }


}
