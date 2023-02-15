package servlet;

import Pojo.Goods;
import Pojo.Page;
import Pojo.Type;
import service.TypeService;
import service.impl.GoodsServiceImpl;
import service.impl.OrderServiceImpl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.impl.TypeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@WebServlet("/adminGoods/*")
public class AdminGoodsServlet extends baseServlet {
    private GoodsServiceImpl goodsService = new GoodsServiceImpl();
    private TypeServiceImpl typeService = new TypeServiceImpl();

    public void goodsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int type = 0;//推荐类型
        if(request.getParameter("type") != null) {
            type=Integer.parseInt(request.getParameter("type") ) ;
        }
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
//        System.out.println("type pageNumber");
//        System.out.println(type);
//        System.out.println(pageNumber);
        Page p=null;
        if(type==0){  // 当type==0 时查询所有商品
            p = goodsService.selectPageByTypeId(0, pageNumber);
        }else{
            p = goodsService.getGoodsRecommendPage(type, pageNumber);
        }
//        System.out.println("page");
//        System.out.println(p.getTotalCount());
//        System.out.println(p.getTotalPage());
//        System.out.println(p.getList());
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = goodsService.getGoodsRecommendPage(type, pageNumber);
            }
        }

        if(request.getSession().getAttribute("typeList")==null){
            List<Type> typeList = typeService.getAllTypes();
            request.getSession().setAttribute("typeList",typeList);
        }

//        System.out.println("typeList");
//        System.out.println(typeList);

        request.setAttribute("p", p);
        request.setAttribute("type", type);
        request.getRequestDispatcher("/admin/goods_list.jsp").forward(request, response);
    }

    // 添加商品
    public void goodsAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            Goods g = new Goods();
            for(FileItem item:list) {
                if(item.isFormField()) {
                    switch(item.getFieldName()) {
                        case "name":
                            g.setName(item.getString("utf-8"));
                            break;
                        case "price":
                            g.setPrice(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "intro":
                            g.setIntro(item.getString("utf-8"));
                            break;
                        case "stock":
                            g.setStock(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "typeid":
                            g.setTypeid(Integer.parseInt(item.getString("utf-8")));
                            break;
                    }
                }else {
                    if(item.getInputStream().available()<=0)continue;
                    String fileName = item.getName();
                    fileName = fileName.substring(fileName.lastIndexOf("."));
                    fileName = "/"+new Date().getTime()+fileName;
                    String path = this.getServletContext().getRealPath("picture")+fileName;
                    InputStream in = item.getInputStream();
                    FileOutputStream out = new FileOutputStream(path);
                    byte[] buffer = new byte[1024];
                    int len=0;
                    while( (len=in.read(buffer))>0 ) {
                        out.write(buffer);
                    }
                    in.close();
                    out.close();
                    item.delete();
                    switch(item.getFieldName()) {
                        case "cover":
                            g.setCover("picture"+fileName);
                            break;
                        case "image1":
                            g.setImage1("picture"+fileName);
                            break;
                        case "image2":
                            g.setImage2("picture"+fileName);
                            break;
                    }
                }
            }
            goodsService.addGoods(g);
            request.getRequestDispatcher("http://localhost:8080/KFC/adminGoods/goodsList").forward(request, response);
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 删除商品
    public void goodsDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        goodsService.deleteGoods(id);
        request.getRequestDispatcher("http://localhost:8080/KFC/adminGoods/goodsList").forward(request, response);
    }

    // 显示要编辑的商品信息
    public void goodsEditShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Goods g = goodsService.getGoodsById(id);
        request.setAttribute("g", g);
        request.getRequestDispatcher("/admin/goods_edit.jsp").forward(request, response);
    }

    // 编辑商品
    public void goodsEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            Goods g = new Goods();
            int pageNumber =1;
            int type=0;
            for(FileItem item:list) {
                if(item.isFormField()) {
                    switch(item.getFieldName()) {
                        case "id":
                            g.setId(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "name":
                            g.setName(item.getString("utf-8"));
                            break;
                        case "price":
                            g.setPrice(Float.parseFloat(item.getString("utf-8")));
                            break;
                        case "intro":
                            g.setIntro(item.getString("utf-8"));
                            break;
                        case "cover":
                            g.setCover(item.getString("utf-8"));
                            break;
                        case "image1":
                            g.setImage1(item.getString("utf-8"));
                            break;
                        case "image2":
                            g.setImage2(item.getString("utf-8"));
                            break;
                        case "stock":
                            g.setStock(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "typeid":
                            g.setTypeid(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "pageNumber":
                            pageNumber=Integer.parseInt(item.getString("utf-8"));
                            break;
                        case "type":
                            type = Integer.parseInt(item.getString("utf-8"));
                            break;
                    }
                }else {
                    if(item.getInputStream().available()<=0)continue;
                    String fileName = item.getName();
                    fileName = fileName.substring(fileName.lastIndexOf("."));
                    fileName = "/"+new Date().getTime()+fileName;
                    String path = this.getServletContext().getRealPath("picture")+fileName;
                    InputStream in = item.getInputStream();
                    FileOutputStream out = new FileOutputStream(path);
                    byte[] buffer = new byte[1024];
                    int len=0;
                    while( (len=in.read(buffer))>0 ) {
                        out.write(buffer);
                    }
                    in.close();
                    out.close();
                    item.delete();
                    switch(item.getFieldName()) {
                        case "cover":
                            g.setCover("picture"+fileName);
                            break;
                        case "image1":
                            g.setImage1("picture"+fileName);
                            break;
                        case "image2":
                            g.setImage2("picture"+fileName);
                            break;
                    }
                }
            }
            goodsService.editGoods(g);
            request.getRequestDispatcher("http://localhost:8080/KFC/adminGoods/goodsList?pageNumber="+pageNumber+"&type="+type).forward(request, response);
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void goodsRecommend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");
        int typeTarget = Integer.parseInt(request.getParameter("typeTarget"));
        if(method.equals("add")){
            goodsService.addRecommend(id,typeTarget);
        }else{
            goodsService.removeRecommend(id, typeTarget);
        }
        request.getRequestDispatcher("http://localhost:8080/KFC/adminGoods/goodsList").forward(request,response);
    }
}
