package mapper;

import Pojo.Goods;
import Pojo.Recommend;
import Pojo.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface GoodsMapper {

    // 问题，type的映射
    // 通过商品查询商品
    @Select("select * from goods where id = #{id}")
    Goods getGoodsById(int id);

    // 获取某类商品的总数
    @Select("select count(*) from goods where type_id = #{typeId}")
    int getCountOfGoodsByTypeId(int typeId);

    @Select("select count(*) from goods")
    int getCountOfAllGoods();

    @Select("select * from goods limit #{begin}, #{pageSize}")
    List<Goods> selectAll(@Param("begin") int begin, @Param("pageSize") int pageSize);

    // 获取指定商品类型的page
    @Select("select * from goods where type_id=#{typeId} limit #{begin}, #{pageSize}")
    List<Goods> selectGoodsByTypeId(@Param("typeId") int typeId, @Param("begin") int begin, @Param("pageSize") int pageSize);

    // 搜索商品,返回商品列表
    @Select("select * from goods where name like #{keyword} limit #{begin}, #{pageSize}")
    List<Goods>selectSearchGoods(@Param("keyword") String keyword,@Param("begin") int begin, @Param("pageSize") int pageSize);

    // 返回符合搜索条件的商品总数
    @Select("select count(*) from goods where name like #{keyword}")
    int getSearchCount(String keyword);

    @Select("select * from goods g, recommend r where r.goods_id=g.id")
    List<Goods>getScrollGoods();

    @Select("select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from recommend r, goods g, type t where r.type=#{recommentType} and r.goods_id=g.id and g.type_id=t.id")
    List<Goods>getGoodsList(int recommendType);

    @Select("select count(*) from recommend where type=#{recommendType}")
    int getRecommendCountOfGoodsByTypeId(int recommendType);

    // 根据商品id获取商品的类型
    @Select("select t.id, t.name from goods g, type t where g.id=#{goodsId} and g.type_id=t.id")
    Type getTypeByGoodsId(int goodsId);

    // 获取商品列表(包含每个商品的类型名)
    @Select("select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from goods g,recommend r,type t where g.id=r.goods_id and g.type_id=t.id  order by g.id limit #{begin},#{pageSize}")
    List<Goods> getGoodsListAndTypeName(@Param("begin") int begin, @Param("pageSize") int pageSize);

    // 根据recommendType 获取商品列表(包含每个商品的类型名)
    @Select("select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from goods g,recommend r,type t where g.id=r.goods_id and g.type_id=t.id and r.type=#{recommendType} order by g.id limit #{begin},#{pageSize}")
    List<Goods> getGoodsByRecommend(@Param("recommendType") int recommendType, @Param("begin") int begin, @Param("pageSize") int pageSize);

    @Select("select * from recommend where type=#{t} and goods_id=#{g.id}")
    Recommend isRecommend(@Param("g")Goods g, @Param("t") int type);

    // 添加商品
    @Insert("insert into goods values(null,#{g.name},#{g.cover},#{g.image1},#{g.image2},#{g.price},#{g.intro},#{g.stock},#{g.type.id})")
    void insert(@Param("g") Goods g);
    // 删除商品
    @Delete("delete from goods where id = #{id}")
    void delete(int id);
    // 编辑商品信息
    @Update("update goods set name=#{g.name},cover=#{g.cover},image1=#{g.image1},image2=#{g.image2},price=#{g.price},intro=#{g.intro},stock=#{g.stock},type_id=#{g.type.id} where id=#{g.id}")
    void update(@Param("g") Goods g);
    // 添加商品的推荐类型
    @Insert("insert into recommend values (null, #{typeTarget},#{goodsId})")
    void addRecommend(@Param("goodsId")int goodsId, @Param("typeTarget")int type);
    // 移除商品的推荐类型
    @Delete("delete from recommend where goods_id=#{goodsId} and type=#{typeTarget}")
    void removeRecommend(@Param("goodsId")int goodsId,@Param("typeTarget")int type);
}
