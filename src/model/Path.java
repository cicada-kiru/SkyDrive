package model;

/**
 * Created by asus on 2017/10/13.
 */
public class Path {
    String username, path, md5, type;

    public Path(String username, String path, String md5, String type) {
        this.username = username;
        this.path = path;
        this.md5 = md5;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
