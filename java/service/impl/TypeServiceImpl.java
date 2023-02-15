package service.impl;

import Pojo.Goods;
import Pojo.Type;
import mapper.GoodsMapper;
import mapper.TypeMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.GoodsService;
import service.TypeService;
import utils.SqlSessionFactoryUtils;

import java.util.List;

public class TypeServiceImpl implements TypeService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    public Type getTypeById(int id) {
        SqlSession sqlSession = factory.openSession();
        TypeMapper mapper = sqlSession.getMapper(TypeMapper.class);
        Type type = new Type();
        type = mapper.selectTypeById(id);
        sqlSession.close();
        return type;
    }

    @Override
    public List<Type> getAllTypes() {
        SqlSession sqlSession = factory.openSession();
        TypeMapper mapper = sqlSession.getMapper(TypeMapper.class);
        List<Type> typeList = mapper.selectAll();
        sqlSession.close();
        return typeList;
    }

    @Override
    public void add(String name) {
        SqlSession sqlSession = factory.openSession();
        TypeMapper mapper = sqlSession.getMapper(TypeMapper.class);
        mapper.insert(name);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void editType(int id, String name) {
        SqlSession sqlSession = factory.openSession();
        TypeMapper mapper = sqlSession.getMapper(TypeMapper.class);
        mapper.update(id, name);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public boolean deleteType(int id) {
        SqlSession sqlSession = factory.openSession();
        TypeMapper mapper = sqlSession.getMapper(TypeMapper.class);
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


}
