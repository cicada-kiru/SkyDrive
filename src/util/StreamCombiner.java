package util;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

/**
 * Created by asus on 2017/10/14.
 */
public class StreamCombiner extends OutputStream {
    private OutputStream out;
    private MessageDigest md5;

    public StreamCombiner(OutputStream out, MessageDigest md5) {
        this.out = out;
        this.md5 = md5;
    }

    public void write(int b) throws IOException {
        out.write(b);
        md5.update((byte) b);
    }
}
