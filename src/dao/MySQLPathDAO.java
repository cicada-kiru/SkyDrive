package dao;

import model.Path;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by asus on 2017/10/13.
 */
public class MySQLPathDAO extends BaseDAO<Path> implements PathDAO {
    private static final PathDAO pathdao = new MySQLPathDAO();

    public static PathDAO getInstance() {
        return pathdao;
    }

    private MySQLPathDAO() {
        super(
            "com.mysql.jdbc.Driver",
            "jdbc:mysql://localhost:3306/yousa",
            "root",
            "dn3369668"
        );
    }

    public Path getPath(String username, String path) {
        return queryModel("select * from path where username = '" + username + "' and path = '" + path + "'");
    }

    public List<Path> getPaths(String username, String path) {
        return queryModels("select * from path where username = '" + username + "' and path like '" + path + "%'");
    }

    public boolean addPath(Path path) {
        return updateModel(
            "insert into path value('" + path.getUsername() + "', '" + path.getPath() + "', '" + path.getMd5() + "', '" + path.getType() + "')"
        );
    }

    public boolean setPath(Path path) {
        return updateModel(
            "update path set username = '" + path.getUsername() + "', path = '" + path.getPath() +"', md5 = '" + path.getMd5()
            + "' where username = '" + path.getUsername() + "' and path = '" + path.getPath() + "' and filetype = '" + path.getType() + "'"
        );
    }

    public boolean removePath(String username, String path) {
        return updateModel("delete from path where username = '" + username + "' and path = '" + path + "'");
    }

    public String[] getAllMd5() {
        ResultSet result = query("select distinct md5 from path");
        try {
            result.last();
            String[] results = new String[result.getRow()];
            result.beforeFirst();
            for (int i = 0;result.next();i ++) {
                results[i] = result.getString(1);
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removePaths(String username, String path) {
        return updateModel("delete from path where username = '" + username + "' and (path like '" + path + "/%' or path = '" + path + "')");
    }
}
