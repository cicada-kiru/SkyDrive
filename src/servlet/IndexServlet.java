package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by asus on 2017/10/14.
 */
@WebServlet(
        urlPatterns = "/index"
)
@MultipartConfig(
        maxFileSize = 1073741824L
)
public class IndexServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("file");
        Scanner in = new Scanner(part.getInputStream());
        while (in.hasNextLine()) {
            System.out.println(in.nextLine());
        }
        resp.setHeader("Content-Disposition", "attachment; filename=123");
        resp.setContentType("application/octet-stream");
        PrintStream out = new PrintStream(resp.getOutputStream());
        out.println("hello world");
        out.close();
    }
}
