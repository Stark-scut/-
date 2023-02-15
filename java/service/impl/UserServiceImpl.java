package service.impl;

import Pojo.Page;
import Pojo.Record;
import Pojo.User;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.UserService;
import utils.SqlSessionFactoryUtils;

import java.util.List;

public class UserServiceImpl implements UserService {

    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    @Override
    public boolean register(String username, String email) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User register = mapper.register(username, email);
        if(register==null) return true;
        sqlSession.close();
        return false;
    }

    @Override
    public void add(User user) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.add(user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public User login(String username, String email) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = null;
        user = mapper.login(username, email);
        sqlSession.close();

        return user;
    }

    @Override
    public void updateInfo(String name, String phone, String address, Integer id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateInfo(name, phone, address, id);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void changePwd(String pwd, Integer id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.changePwd(pwd, id);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public int getUserCount() {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int count = mapper.getUserCount();
        sqlSession.close();
        return count;
    }


    @Override
    public Page getUserPage(int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 7;
        int begin = (pageNumber-1)*pageSize;
        int totalCount = 0;
        try {
            totalCount = getUserCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.SetPageSizeAndTotalCount(pageSize,totalCount);
        List list = null;
        try {
            list = mapper.selectUserList(begin,pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.setList(list);
        return p;
    }

    @Override
    public void editUser(User user) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.update(user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public User getUserById(int id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectUserById(id);
        sqlSession.close();
        return user;
    }

    @Override
    public boolean deleteUser(int id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        try {
            mapper.delete(id);
            sqlSession.commit();
            sqlSession.close();
            return true;
        } catch (Exception e) {
            sqlSession.close();
            return false;
        }
    }

    @Override
    public void addRecord(Record record) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        try {
            mapper.addRecord(record);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sqlSession.commit();
        sqlSession.close();
        return;
    }

    @Override
    public Page getRecordPage(int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 10;
        int totalCount = 0;
        int begin = (pageNumber-1)*pageSize;
        try {
            totalCount = mapper.getRecordCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.SetPageSizeAndTotalCount(pageSize, totalCount);

        List list = null;
        list = mapper.getRecordList(begin,pageSize);
        for(Record record : (List<Record>)list){
            record.setUserName(mapper.getUserNameByRecordId(record.getId()));
        }
        p.setList(list);
        sqlSession.close();
        return p;

    }


}
