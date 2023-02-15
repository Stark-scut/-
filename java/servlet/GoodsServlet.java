package servlet;

import Pojo.*;
import service.TypeService;
import service.impl.GoodsServiceImpl;
import service.impl.TypeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@WebServlet("/goods/*")
public class GoodsServlet extends baseServlet {

    private GoodsServiceImpl goodsService = new GoodsServiceImpl();
    private TypeService typeService = new TypeServiceImpl();

    // 向购物车的订单中添加商品
    public void goodsBuy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = null;
        if(request.getSession().getAttribute("order")!=null){  // 已存在订单，即购物车内有商品
            order = (Order) request.getSession().getAttribute("order");
        }else{
            order = new Order();      // 不存在订单，购物车为空，新建一个订单
            request.getSession().setAttribute("order", order);
        }
        int goodsId = Integer.parseInt(request.getParameter("goodsid"));
        Goods goods = goodsService.getGoodsById(goodsId);
        if(goods.getStock()>0){
            order.addGoods(goods);       // 库存大于0，将商品添加至订单
            response.getWriter().print("ok");
        }else{
            response.getWriter().print("fail");
        }
    }

    // 删除购物车的商品
    public void goodsDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = (Order) request.getSession().getAttribute("order");
        int goodsId = Integer.parseInt(request.getParameter("goodsid"));
        order.delete(goodsId);
        response.getWriter().print("ok");
    }


    // 分页显示商品数据
    public void GoodsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("----GoodList被请求了-----");
        int typeId=0;
        if(request.getParameter("typeid")!=null)
        {
            typeId=Integer.parseInt(request.getParameter("typeid"));
        }
        System.out.println("typeid="+typeId);
        int pageNumber=1;            // 商品页数
        if(request.getParameter("pageNumber")!=null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber"));
            }
            catch (Exception e) {
            }
        }
        Type t=null;
        if(typeId!=0)
        {
            t=typeService.getTypeById(typeId);
        }
        request.setAttribute("t",t);
        //List<Goods> list=gService.selectGoodsByTypeID(id,1,8);
        //request.setAttribute("goodsList",list);
        if(pageNumber<=0)
            pageNumber=1;

        // 根据typeId获取商品，并对获取到的商品集合进行分页
        Page p=goodsService.selectPageByTypeId(typeId,pageNumber);

        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p=goodsService.selectPageByTypeId(typeId,p.getTotalPage());
            }
        }
        System.out.println(p);
//        System.out.println(p.getList());
//        System.out.println(p.getTotalCount());

        if(request.getSession().getAttribute("typeList")==null){
            List<Type> typeList = typeService.getAllTypes();
            request.getSession().setAttribute("typeList",typeList);
        }
//        request.getSession().removeAttribute("typeList");
//        List<Type> typeList = typeService.getAllTypes();
//        request.getSession().setAttribute("typeList",typeList);
//        System.out.println("typeList");
//        System.out.println(typeList);
        request.setAttribute("p",p);
        request.setAttribute("id",String.valueOf(typeId));
        request.getRequestDispatcher("/goods_list.jsp").forward(request,response);
        return;
    }

    public void goodsSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取搜索框中的关键字

        String keyword = request.getParameter("keyword");
        keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
        System.out.println(keyword);

        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            }
            catch (Exception e){
            }
        }
        if(pageNumber<=0)
        {
            pageNumber=1;
        }


        Page p =goodsService.getSearchGoodsPage(keyword,pageNumber);

        if(p.getTotalPage()==0)   // 没有匹配的商品
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p =goodsService.getSearchGoodsPage(keyword,pageNumber);
            }
        }
        System.out.println("-----list-------");
        System.out.println(p.getList());
        request.setAttribute("p", p);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/goods_search.jsp").forward(request, response);
        return;
    }

    public void goodsDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("goodsDetail被访问了");
        int id = Integer.parseInt(request.getParameter("id"));
        Goods goods = goodsService.getGoodsById(id);
        Type type  = goodsService.getTypeByGoodsId(id);

        System.out.println(type.getName());
        System.out.println(type.getId());
//        System.out.println(id);
//        System.out.println(typeName);
        goods.setType(type);
        request.setAttribute("g", goods);
        System.out.println(goods);
        request.getRequestDispatcher("/goods_detail.jsp").forward(request, response);
        return;
    }

    public void goodsLessen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = (Order) request.getSession().getAttribute("order");
        int goodsid = Integer.parseInt(request.getParameter("goodsid"));
        order.lessen(goodsid);
        response.getWriter().print("ok");
        return;
    }

    public void goodsIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Goods> scrollGoods = goodsService.getScrollGoods();
        request.setAttribute("scroll",scrollGoods);

        List<Goods> newList = goodsService.getGoodsList(3);   // 3 为新品
//        System.out.println("-------newList-------");
        request.setAttribute("newList",newList);
//        System.out.println(newList.get(0).getType().getName());
//        System.out.println(newList);

        List<Goods> hotList=goodsService.getGoodsList(2);     // 2 为热销品
//        System.out.println("-------hotList---------");
        request.setAttribute("hotList",hotList);
//        System.out.println(hotList);
        request.getRequestDispatcher("/index.jsp").forward(request,response);
        return;
    }

    public void goodsRecommendList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int type = Integer.parseInt(request.getParameter("type")) ;
        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            }
            catch (Exception e) {
            }
        }
        if(pageNumber<=0)
            pageNumber=1;
        Page p = goodsService.getGoodsRecommendPage(type, pageNumber);

        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = goodsService.getGoodsRecommendPage(type, p.getTotalPage());
            }
        }
        request.setAttribute("p", p);
        request.setAttribute("t", type);
        request.getRequestDispatcher("/goodsrecommend_list.jsp").forward(request, response);
    }

}
