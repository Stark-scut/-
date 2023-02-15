package service;

import Pojo.Page;
import Pojo.Record;
import Pojo.User;
import mapper.UserMapper;

import java.util.List;

public interface UserService {

    boolean register(String username,String email);

    void add(User user);

    User login(String username, String email);

    void updateInfo(String name, String phone, String address, Integer id);
    void changePwd(String pwd, Integer id);

    int getUserCount();


    Page getUserPage(int pageNumber);
    void editUser(User user);

    User getUserById(int id);

    boolean deleteUser(int id);

    void addRecord(Record record);

    Page getRecordPage(int pageNumber);
}
