package servlet;

import Pojo.Page;
import Pojo.User;
import service.UserService;
import service.impl.OrderServiceImpl;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminUser/*")
public class AdminUserServlet extends baseServlet {

    private UserServiceImpl userService = new UserServiceImpl();


    public void userList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        Page p = userService.getUserPage(pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = userService.getUserPage(pageNumber);
            }
        }
        request.setAttribute("p", p);
        request.getRequestDispatcher("/admin/user_list.jsp").forward(request, response);
    }

    public void userAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        user.setAdmin(false);
        user.setValidate(true);
        if(userService.register(username, email)){
            request.setAttribute("msg","客户添加成功！");
            userService.add(user);
            request.getRequestDispatcher("http://localhost:8080/KFC/adminUser/userList").forward(request,response);
        }else{
            request.setAttribute("failMsg","用户名或邮箱重复，请重新填写！");
            request.setAttribute("u",user);
            request.getRequestDispatcher("/admin/user_add.jsp").forward(request,response);
        }
    }

    public void userEditShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUserById(id);
        request.setAttribute("u",user);
        request.getRequestDispatcher("/admin/user_edit.jsp").forward(request,response);
    }

    public void userEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);

        userService.editUser(user);
        request.getRequestDispatcher("http://localhost:8080/KFC/adminUser/userList").forward(request,response);
    }

    public void userDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isSuccess = userService.deleteUser(id);
        if(isSuccess){
            request.setAttribute("msg","客户删除成功");
        }else{
            request.setAttribute("failMsg","客户下有订单，请先删除该客户下的订单，再删除该客户！");
        }
        request.getRequestDispatcher("http://localhost:8080/KFC/adminUser/userList").forward(request,response);
    }

    public void userReset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String password = request.getParameter("password");
        userService.changePwd(password,id);
        request.getRequestDispatcher("http://localhost:8080/KFC/adminUser/userList").forward(request,response);
    }

    public void recordList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        Page p = userService.getRecordPage(pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = userService.getRecordPage(pageNumber);
            }
        }

        request.setAttribute("p", p);
        request.getRequestDispatcher("/admin/record_list.jsp").forward(request, response);
    }


}
