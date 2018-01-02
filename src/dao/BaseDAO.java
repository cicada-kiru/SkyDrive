package dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/10/13.
 */
public abstract class BaseDAO<T> {
    private String driver, url, user, password, table;
    private Class<?> clazz;
    private Connection connect;
    private Statement state;
    private ResultSet result;

    public BaseDAO(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            String type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            clazz = Class.forName(type);
            table = clazz.getSimpleName().toLowerCase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected void init() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.getResultSet();
    }

    public T queryModel(String sql) {
        try {
            init();
            result = state.executeQuery(sql);
            Constructor<?> ctor = clazz.getConstructors()[0];
            Object[] args = new Object[ctor.getParameterCount()];
            result.next();
            for (int i = 0;i < args.length;i ++)
                args[i] = result.getString(i + 1);
            return (T) ctor.newInstance(args);
        } catch (ReflectiveOperationException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    public List<T> queryModels(String sql) {
        try {
            init();
            result = state.executeQuery(sql);
            Constructor<?> ctor = clazz.getConstructors()[0];
            Object[] args = new Object[ctor.getParameterCount()];
            List<T> models = new ArrayList<>();
            while (result.next()) {
                for (int i = 0; i < args.length; i++)
                    args[i] = result.getString(i + 1);
                models.add((T) ctor.newInstance(args));
            }
            return models;
        } catch (ReflectiveOperationException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    public boolean updateModel(String sql) {
        try {
            init();
            if (state.executeUpdate(sql) > 0) return true;
            return false;
        } catch (ReflectiveOperationException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    public ResultSet query(String sql) {
        try {
            init();
            return state.executeQuery(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void close() {
        if (result != null) try {
            result.close();
        } catch (SQLException e) {
            System.err.println("close ResultSet exception");
        }
        if (state != null) try {
            state.close();
        } catch (SQLException e) {
            System.err.println("close Statement exception");
        }
        if (connect != null) try {
            connect.close();
        } catch (SQLException e) {
            System.err.println("close Connection exception");
        }
    }
}
