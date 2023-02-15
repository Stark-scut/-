package servlet;

import Pojo.Record;
import Pojo.User;
import com.alibaba.fastjson.JSON;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/user/*")
public class userServlet extends baseServlet {
    private UserServiceImpl userService = new UserServiceImpl();

    // 用户注册
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        boolean flag = userService.register(username, email);
        System.out.println(flag);
        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(request.getParameter("password"));
        user.setName(request.getParameter("name"));
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));
        user.setAdmin(false);
        user.setValidate(true);

//        System.out.println(user);

        if(flag){
            userService.add(user);
            request.setAttribute("msg","注册成功，请登录");
            request.getRequestDispatcher("/user_login.jsp").forward(request, response);
        }else{
            request.setAttribute("msg", "用户名或邮箱重复，请重新填写！");
            request.getRequestDispatcher("/user_register.jsp").forward(request, response);
        }
    }


    // 用户登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        System.out.println(username);
//        System.out.println(password);
        User user = new User();
        user = userService.login(username, password);
//        System.out.println(user);
        if(user==null){
//            System.out.println("重新登录");
            request.setAttribute("failMsg","用户名、邮箱或密码错误，请重新登录");
            request.getRequestDispatcher("/user_login.jsp").forward(request, response);
        }else{
//            System.out.println("登录成功");
            request.getSession().setAttribute("user", user);
            Record record = new Record();
            record.setUser(user);
            record.setStartTime(new Date());
            request.getSession().setAttribute("record", record);


//            System.out.println("hhhhhh");
            request.getRequestDispatcher("/user_center.jsp").forward(request, response);
//            System.out.println("zzzzz");
        }

    }


    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("user");
        Record record = (Record) request.getSession().getAttribute("record");
        record.setEndTime(new Date());
        userService.addRecord(record);
        request.getSession().removeAttribute("record");
        response.sendRedirect("http://localhost:8080/KFC/goods/goodsIndex");
    }

    public void changeInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        user.setUserName(name);
        user.setPhone(phone);
        user.setAddress(address);
        userService.updateInfo(name, phone, address, user.getId());
        request.setAttribute("msg", "收件信息更新成功");
        request.getRequestDispatcher("/user_center.jsp").forward(request, response);
    }

    public void changePwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String password = request.getParameter("password");
        String newPwd = request.getParameter("newPassword");
        if(password.equals(user.getPassword())){
            user.setPassword(newPwd);
            userService.changePwd(newPwd, user.getId());
            request.setAttribute("msg","修改成功");
            request.getRequestDispatcher("/user_center.jsp").forward(request, response);
        }else{
            request.setAttribute("failMsg", "修改失败，原密码不正确，你再想想！");
            request.getRequestDispatcher("/user_center.jsp").forward(request, response);
        }
    }

}
