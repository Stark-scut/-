package servlet;

import Pojo.Page;
import Pojo.Type;
import Pojo.User;
import service.impl.TypeServiceImpl;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminType/*")
public class AdminTypeServlet extends baseServlet {

    private TypeServiceImpl typeService = new TypeServiceImpl();

    public void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Type> list= typeService.getAllTypes();
        request.setAttribute("list", list);
        this.getServletContext().removeAttribute("typeList");
        this.getServletContext().setAttribute("typeList",list);
        request.getRequestDispatcher("/admin/type_list.jsp").forward(request, response);

    }

    public void typeAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        try {
            typeService.add(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("http://localhost:8080/KFC/adminType/typeList").forward(request, response);
    }

    public void typeEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            typeService.editType(id, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("http://localhost:8080/KFC/adminType/typeList").forward(request, response);
    }

    public void typeDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isSuccess = typeService.deleteType(id);
        if(isSuccess){
            request.setAttribute("msg","删除成功");
        }else{
            request.setAttribute("failMsg","类目下包含商品，无法直接删除类目");
        }
        request.getRequestDispatcher("http://localhost:8080/KFC/adminType/typeList").forward(request, response);
    }

}
