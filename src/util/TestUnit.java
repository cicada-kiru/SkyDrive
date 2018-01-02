package util;

import dao.MySQLPathDAO;
import dao.MySQLUserDAO;
import dao.PathDAO;
import model.Path;
import model.User;
import service.SkyDrive;
import service.ThousandSkyDrive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by asus on 2017/10/13.
 */
public class TestUnit {
    public static void main(String[] args) throws Exception {
//        MySQLUserDAO dao = new MySQLUserDAO();
//        User user = new User("yousa", "123456");
//        System.out.println(dao.addUser(user));
//        System.out.println(dao.getUser("yousa").getPassword());
//        PathDAO dao = new MySQLPathDAO();
//        Path path = new Path("yousa", "/", "rnf8420fni2");
////        dao.addPath(path);
//        System.out.println(dao.setPath(path));
//        System.out.println(dao.getPaths("yousa", "/").get(0).getMd5());
//        System.out.println(dao.removePath("yousa","/"));
        InputStream in = new FileInputStream("./web/index.jsp");
//        HdfsUtil hdfs = HdfsUtilImpl.getInstance();
        OutputStream out = new FileOutputStream("./nb123");
//        System.out.println(hdfs.downloadFile("/nb123", out));
//        System.out.println(hdfs.createDir("/dir"));
//        System.out.println(hdfs.uploadFile("/nb123", in));
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = in.read(buffer)) != -1) {
//            md5.update(buffer);
//        }
//        BigInteger integer = new BigInteger(1, md5.digest());
//        System.out.println(integer.toString(16));

        SkyDrive drive = ThousandSkyDrive.getInstance();
//        System.out.println(drive.uploadFile("yousa", "/file", in));

//        System.out.println(drive.downloadFile("yousa", "/file", out));
        System.out.println(drive.changeDir("yousa", "/test"));
        PathDAO pathdao = MySQLPathDAO.getInstance();
        System.out.println(pathdao.getPaths("yousa", "/"));
    }
}
