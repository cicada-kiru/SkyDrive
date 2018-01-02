package dao;

import model.User;

/**
 * Created by asus on 2017/10/13.
 */
public interface UserDAO {
    User getUser(String username);

    boolean addUser(User user);
}
