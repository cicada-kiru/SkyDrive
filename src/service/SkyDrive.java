package service;

import model.Path;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by asus on 2017/10/13.
 */
public interface SkyDrive {
    boolean signIn(String username, String password);

    void signOut(String username);

    boolean signUp(String username, String password);

    boolean createDir(String username, String dir);

    boolean uploadFile(String username, String file, InputStream in);

    boolean downloadFile(String username, String file, OutputStream out);

    List<Path> changeDir(String username, String dir);

    boolean removeDir(String username, String dir);

    boolean removeFile(String username, String file);
}
