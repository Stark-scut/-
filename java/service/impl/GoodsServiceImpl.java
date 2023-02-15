package service.impl;

import Pojo.Goods;
import Pojo.Page;
import Pojo.Recommend;
import Pojo.Type;
import mapper.GoodsMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.GoodsService;
import utils.SqlSessionFactoryUtils;

import java.sql.SQLException;
import java.util.List;

public class GoodsServiceImpl implements GoodsService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    @Override
    public Goods getGoodsById(int id) {
        Goods goods = null;
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        goods = mapper.getGoodsById(id);
        sqlSession.close();
        return goods;
    }

    @Override
    public int getCountOfGoodsByTypeId(int typeId) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        int counts = 0;
        if(typeId==0){
            try {
                counts = mapper.getCountOfAllGoods();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                counts = mapper.getCountOfGoodsByTypeId(typeId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        sqlSession.close();
        return counts;
    }

    @Override
    public List<Goods> selectGoodsByTypeId(int typeId, int pageNumber, int pageSize) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        int begin = (pageNumber-1)*pageSize;
        List<Goods> goods = null;
        if(typeId==0){
            try {
                goods = mapper.selectAll(begin, pageSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                goods = mapper.selectGoodsByTypeId(typeId, begin, pageSize);   //  当typeId为0时返回全部商品数的总数
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        sqlSession.close();
        return goods;
    }

    @Override
    public Page selectPageByTypeId(int typeId, int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);

        Page page = new Page();
        page.setPageNumber(pageNumber);

        int totalCount=0;
        try {
            totalCount = getCountOfGoodsByTypeId(typeId);    //  当typeId为0时返回全部商品数的总数
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 设置pageSize 和商品总数并据此算出 totalPage
        page.SetPageSizeAndTotalCount(8, totalCount);

        List list = null;
        try {
            list = selectGoodsByTypeId(typeId, pageNumber,8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            for(Goods g : (List<Goods>)list) {
                g.setScroll(isScroll(g));
                g.setHot(isHot(g));
                g.setNew(isNew(g));
                g.setTypename(mapper.getTypeByGoodsId(g.getId()).getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        page.setList(list);
        sqlSession.close();
        return page;
    }

    @Override
    public Page getSearchGoodsPage(String keyword, int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);

        // 一个page就是一个展示的页
        Page page = new Page();
        page.setPageNumber(pageNumber);
        int totalCount = 0;
        keyword = "%"+keyword+"%";
        try {
            totalCount = mapper.getSearchCount(keyword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int pageSize = 8;
        page.SetPageSizeAndTotalCount(pageSize,totalCount);
        List list = null;
        int begin = (pageNumber-1)*pageSize;
        try {
            list = mapper.selectSearchGoods(keyword,begin,pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        page.setList(list);
        sqlSession.close();
        return page;
    }

    @Override
    public List<Goods> getScrollGoods() {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        List<Goods> scrollGoods = mapper.getScrollGoods();
        sqlSession.close();
        return scrollGoods;
    }

    @Override
    public List<Goods> getGoodsList(int recommendType) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        List<Goods> goodsList = mapper.getGoodsList(recommendType);
        sqlSession.close();
        return goodsList;
    }

    @Override
    public int getRecommendCountOfGoodsByTypeId(int recommendType) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        int counts = mapper.getRecommendCountOfGoodsByTypeId(recommendType);
        return counts;
    }


    @Override
    public List<Goods> selectGoodsByRecommend(int recommendType, int pageNumber, int pageSize) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        List<Goods> list = null;
        int begin = (pageNumber-1)*pageSize;
        if(recommendType==0){
            try {
                list = mapper.getGoodsListAndTypeName(begin, pageSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                list = mapper.getGoodsByRecommend(recommendType, begin, pageSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public boolean isRecommend(Goods g, int recommendType) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        Recommend recommend = mapper.isRecommend(g, recommendType);
        sqlSession.close();
        if(recommend!=null) return true;
        return false;
    }

    @Override
    public boolean isScroll(Goods g) {
        return isRecommend(g, 1);
    }

    @Override
    public boolean isHot(Goods g) {
        return isRecommend(g, 2);
    }

    @Override
    public boolean isNew(Goods g) {
        return isRecommend(g, 3);
    }

    @Override
    public void addGoods(Goods g) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        mapper.insert(g);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void deleteGoods(int id) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        mapper.delete(id);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void editGoods(Goods g) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        mapper.update(g);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void addRecommend(int id, int type) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        mapper.addRecommend(id, type);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void removeRecommend(int id, int type) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        mapper.removeRecommend(id, type);
        sqlSession.commit();
        sqlSession.close();
    }


    public Page getGoodsRecommendPage(int recommendType, int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);

        Page p = new Page();
        p.setPageNumber(pageNumber);
        int totalCount = 0;

        try {
            totalCount = getRecommendCountOfGoodsByTypeId(recommendType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        p.SetPageSizeAndTotalCount(8, totalCount);
        List list=null;


        try {
            list = selectGoodsByRecommend(recommendType, pageNumber, 8);
            for(Goods g : (List<Goods>)list) {
                g.setScroll(isScroll(g));
                g.setHot(isHot(g));
                g.setNew(isNew(g));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        p.setList(list);
        return p;
    }




    // 根据商品id获取商品的类型
    @Override
    public Type getTypeByGoodsId(int goodsId) {
        SqlSession sqlSession = factory.openSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);

        Type type = mapper.getTypeByGoodsId(goodsId);
        sqlSession.close();
        return type;
    }


}
