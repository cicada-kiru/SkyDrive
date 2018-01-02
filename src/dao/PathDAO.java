package dao;

import model.Path;

import java.util.List;

/**
 * Created by asus on 2017/10/13.
 */
public interface PathDAO {
    Path getPath(String username, String path);

    List<Path> getPaths(String username, String path);

    boolean addPath(Path path);

    boolean setPath(Path path);

    boolean removePath(String username, String path);

    boolean removePaths(String username, String path);

    String[] getAllMd5();
}
