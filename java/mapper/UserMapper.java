package mapper;

import Pojo.User;
import org.apache.ibatis.annotations.*;
import Pojo.Record;
import java.util.List;

public interface UserMapper {

    // 用户注册
    @ResultMap("userResultMap")
    @Select("select * from user where username=#{username} or email=#{email}")
    User register(@Param("username") String username, @Param("email") String email);

    // 注册成功后添加用户数据
    @Insert("insert into user values(null,#{userName},#{email},#{password},#{name},#{phone},#{address},#{admin},#{validate})")
    void add(User user);

    // 用户登录
    @ResultMap("userResultMap")
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(@Param("username")String username,@Param("password")String password);

    @Update("update user set name=#{name},phone=#{phone},address=#{address} where id = #{id}")
    void updateInfo(@Param("name")String name, @Param("phone")String phone, @Param("address")String address,@Param("id")Integer id);

    @Update("update user set password=#{pwd} where id=#{id}")
    void changePwd(@Param("pwd") String pwd, @Param("id") Integer id);

    @Select("select count(*) from user")
    int getUserCount();

    @ResultMap("userResultMap")
    @Select("select * from user limit #{begin},#{pageSize}")
    List<User> selectUserList(@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Update("update user set name=#{u.name},phone=#{u.phone},address=#{u.address} where id=#{u.id}")
    void update(@Param("u")User u);

    @ResultMap("userResultMap")
    @Select("select * from user where id = #{id}")
    User selectUserById(int id);

    @Delete("delete from user where id=#{id}")
    void delete(int id);

    @Insert("insert into record values (null,#{r.startTime},#{r.endTime},#{r.user.id})")
    void addRecord(@Param("r")Record record);

    @Select("select count(*) from record")
    int getRecordCount();

    @Select("select id,start_time startTime,end_time endTime from record limit #{begin},#{pageSize}")
    List<Record>getRecordList(@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Select("select username from record,user where record.user_id=user.id and record.id=#{recordId}")
    String getUserNameByRecordId(int recordId);
}
