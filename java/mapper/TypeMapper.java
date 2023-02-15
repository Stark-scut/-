package mapper;

import Pojo.Goods;
import Pojo.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TypeMapper {

    // 问题，type的映射
    // 通过商品查询商品

    @Select("select * from type where id = #{id}")
    Type selectTypeById(int id);

    @Select("select * from type")
    List<Type> selectAll();
    @Insert("insert into type values (null,#{name})")
    void insert(String name);
    @Update("update type set name=#{name} where id=#{id}")
    void update(@Param("id")int id, @Param("name")String name);

    @Delete("delete from type where id=#{id}")
    void delete(int id);

}
