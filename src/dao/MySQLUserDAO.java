package dao;

import model.User;

/**
 * Created by asus on 2017/10/13.
 */
public class MySQLUserDAO extends BaseDAO<User> implements UserDAO {
    private static final UserDAO userdao = new MySQLUserDAO();

    public static UserDAO getInstance() {
        return userdao;
    }

    private MySQLUserDAO() {
        super (
            "com.mysql.jdbc.Driver",
            "jdbc:mysql://localhost:3306/yousa",
            "root",
            "dn3369668"
        );
    }

    public User getUser(String username) {
        return queryModel("select * from users where username = '" + username + "'");
    }

    public boolean addUser(User user) {
        return updateModel("insert into users values('" + user.getUsername() + "', '" + user.getPassword() + "')");
    }
}
