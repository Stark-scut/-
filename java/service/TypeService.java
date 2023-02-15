package service;


import Pojo.Type;

import java.util.List;

public interface TypeService {
    Type getTypeById(int id);
    List<Type> getAllTypes();

    void add(String name);
    void editType(int id, String name);
    boolean deleteType(int id);
}
