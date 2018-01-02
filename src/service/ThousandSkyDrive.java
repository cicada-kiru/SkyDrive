package service;

import dao.MySQLPathDAO;
import dao.MySQLUserDAO;
import dao.PathDAO;
import dao.UserDAO;
import model.Path;
import model.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import util.StreamCombiner;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * Created by asus on 2017/10/14.
 */
public class ThousandSkyDrive implements SkyDrive {
    private static SkyDrive drive = new ThousandSkyDrive();
    private static String home = "/SkyDrive/";

    public static SkyDrive getInstance() {
        return drive;
    }

    private FileSystem fs;

    private UserDAO userdao;
    private PathDAO pathdao;
    private Set<String> users = new ConcurrentSkipListSet<>();
    private Set<String> files = new ConcurrentSkipListSet<>();

    private ThousandSkyDrive() {
        try {
            fs = FileSystem.get(URI.create("hdfs://localhost:9000/"), new Configuration());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        userdao = MySQLUserDAO.getInstance();
        pathdao = MySQLPathDAO.getInstance();
        Collections.addAll(files, pathdao.getAllMd5());
        files.remove("-1");
    }

    public boolean signIn(String username, String password) {
        if (users.contains(username)) return false;
        User user = userdao.getUser(username);
        if (user == null || !user.getPassword().equals(password)) return false;
        users.add(username);
        return true;
    }

    public void signOut(String username) {
        users.remove(username);
    }

    public boolean signUp(String username, String password) {
        return userdao.addUser(new User(username, password));
    }

    public boolean createDir(String username, String dir) {
        Path path = new Path(username, dir, "-1", "d");
        return pathdao.addPath(path);
    }

    public boolean uploadFile(String username, String file, InputStream in) {
        OutputStream out = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            org.apache.hadoop.fs.Path real = new org.apache.hadoop.fs.Path(home + System.nanoTime());
            FSDataOutputStream fsout = fs.create(real);
            out = new StreamCombiner(fsout, md);
            IOUtils.copyBytes(in, out, 4096, false);
            String md5 = new BigInteger(1, md.digest()).toString(16);
            if (files.contains(md5)) {
                fs.delete(real, true);
                return pathdao.addPath(new Path(username, file, md5, "f"));
            }
            files.add(md5);
            fs.rename(real, new org.apache.hadoop.fs.Path(home + md5));
            fsout.hflush();
            Path path = new Path(username, file, md5, "f");
            return pathdao.addPath(path);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(in, out);
        }
    }

    public boolean downloadFile(String username, String file, OutputStream out) {
        Path path = pathdao.getPath(username, file);
        if (path == null) return false;
        InputStream in = null;
        try {
            in = fs.open(new org.apache.hadoop.fs.Path(home + path.getMd5()));
            IOUtils.copyBytes(in, out, 4096, false);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(in, out);
        }
    }

    public List<Path> changeDir(String username, String dir) {
        if (!dir.endsWith("/")) dir = dir + "/";
        List<Path> paths = pathdao.getPaths(username, dir);
        if (paths == null) return null;
        String valid = dir;
        return paths.stream().filter(path -> path.getPath().matches(valid + "(?!.*?/.*?).*?")).collect(Collectors.toList());
    }

    public boolean removeDir(String username, String dir) {
        Path path = pathdao.getPath(username, dir);
        if (path == null || path.getType().equals("f")) return false;
        return pathdao.removePaths(username, dir);
    }

    public boolean removeFile(String username, String file) {
        Path path = pathdao.getPath(username, file);
        if (path == null || path.getType().equals("d")) return false;
        return pathdao.removePath(username, file);
    }

    private void close(Closeable...streams) {
        for (Closeable stream : streams) {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
