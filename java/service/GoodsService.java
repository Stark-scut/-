package service;

import Pojo.Goods;
import Pojo.Page;
import Pojo.Recommend;
import Pojo.Type;

import java.util.List;

public interface GoodsService {

    // 根据id获取对应商品
    Goods getGoodsById(int id);

    // 获取某类型的商品总数
    int getCountOfGoodsByTypeId(int typeId);

    // 获取某类型的所有商品
    List<Goods> selectGoodsByTypeId(int typeId, int pageNumber, int pageSize);


    Page selectPageByTypeId(int typeId, int pageNumber);

    Page getSearchGoodsPage(String keyword, int pageNumber);

    // 获取滚动的推荐商品
    List<Goods>getScrollGoods();

    // 根据推荐类型获取商品列表
    List<Goods>getGoodsList(int recommendType);

    // 获取指定推荐类型的商品总数
    int getRecommendCountOfGoodsByTypeId(int recommendType);

    Page getGoodsRecommendPage(int recommendType, int pageNumber);

    Type getTypeByGoodsId(int goodsId);

    List<Goods>selectGoodsByRecommend(int recommendType, int pageNumber, int pageSize);

    boolean isRecommend(Goods g, int recommendType);

    boolean isScroll(Goods g);
    boolean isHot(Goods g);
    boolean isNew(Goods g);

    // 添加商品
    void addGoods(Goods g);
    // 删除商品
    void deleteGoods(int id);
    // 编辑商品
    void editGoods(Goods g);
    // 添加商品推荐类型
    void addRecommend(int id, int type);
    // 移除商品推荐类型
    void removeRecommend(int id, int type);
}
