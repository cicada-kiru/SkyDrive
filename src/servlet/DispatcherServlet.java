package servlet;

import service.SkyDrive;
import service.ThousandSkyDrive;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by asus on 2017/10/15.
 */
@WebServlet("/")
@MultipartConfig(maxFileSize = 1073741824L)
public class DispatcherServlet extends HttpServlet {
    private SkyDrive drive = ThousandSkyDrive.getInstance();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        String username = (String) req.getSession().getAttribute("username");
        if (username == null && url.equals("/signUp")) {
            req.getRequestDispatcher("/WEB-INF/views/sign_up.jsp").forward(req, resp);
        } else if (username == null && url.equals("/") || url.equals("/signIn")) {
            req.getRequestDispatcher("/WEB-INF/views/sign_in.jsp").forward(req, resp);
        } else if (username != null && url.equals("/skyDrive")) {
            req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
        } else {
            resp.sendError(404);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        switch (url) {
            case "/signIn":     signIn(req, resp);      break;
            case "/signUp":     signUp(req, resp);      break;
            case "/cd":         changeDir(req, resp);   break;
            case "/mkdir":      createDir(req, resp);   break;
            case "/upload":     upload(req, resp);      break;
            case "/download":   download(req, resp);    break;
            case "/rmf":        removeFile(req, resp);  break;
            case "/rmd":        removeDir(req, resp);   break;
            case "/prev":       prevDir(req, resp);     break;
            case "/signOut":    signOut(req, resp);     break;
            default: resp.sendError(404);
        }
    }

    private void signIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (drive.signIn(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("path", "/");
            session.setAttribute("list", drive.changeDir(username, "/"));
            req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/sign_in.jsp").forward(req, resp);
        }
    }

    private void signUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (drive.signUp(username, password)) {
            req.getRequestDispatcher("/WEB-INF/views/sign_in.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/sign_up.jsp").forward(req, resp);
        }
    }

    private void signOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        drive.signOut((String) session.getAttribute("username"));
        session.removeAttribute("username");
        req.getRequestDispatcher("/WEB-INF/views/sign_in.jsp").forward(req, resp);
    }

    private void changeDir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String path = req.getParameter("path");
        String username = (String) session.getAttribute("username");
        session.setAttribute("path", path);
        session.setAttribute("list", drive.changeDir(username, path));
        req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
    }

    private void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String current = (String) session.getAttribute("path");
        if (!current.endsWith("/")) current = current + "/";
        String path = req.getParameter("path");
        Part part = req.getPart("file");
        if (drive.uploadFile(username, current + path, part.getInputStream())) {
            session.setAttribute("list", drive.changeDir(username, (String) session.getAttribute("path")));
            req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
        } else {
            resp.sendError(500, "UploadFailed");
        }
    }

    private void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String path = req.getParameter("path");
        String filename = path.substring(path.lastIndexOf("/"));
        resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
        resp.setContentType("application/octet-stream");
        drive.downloadFile(username, path, resp.getOutputStream());
    }

    private void removeFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String file = req.getParameter("file");
        String path = (String) session.getAttribute("path");
        drive.removeFile(username, file);
        session.setAttribute("list", drive.changeDir(username, path));
        req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
    }

    private void removeDir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String dir = req.getParameter("dir");
        String path = (String) session.getAttribute("path");
        drive.removeDir(username, dir);
        session.setAttribute("list", drive.changeDir(username, path));
        req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
    }

    private void createDir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dir = req.getParameter("dir");
        if (dir.contains("/")) {
            req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
            return;
        }
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String path = (String) session.getAttribute("path");
        if (!path.endsWith("/")) path = path + "/";
        if (drive.createDir(username, path + dir)) {
            session.setAttribute("list", drive.changeDir(username, (String) session.getAttribute("path")));
            req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
        }
    }

    private void prevDir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String path = (String) session.getAttribute("path");
        if (path.equals("/")) {
            req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
            return;
        }
        String new_path = path.substring(0, path.lastIndexOf("/") + 1);
        if (new_path.endsWith("/")) new_path = new_path.substring(0, new_path.length() - 1);
        session.setAttribute("path", new_path);
        session.setAttribute("list", drive.changeDir(username, (String) session.getAttribute("path")));
        req.getRequestDispatcher("/WEB-INF/views/sky_drive.jsp").forward(req, resp);
    }
}
