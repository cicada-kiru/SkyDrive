<%@ page import="java.util.List" %>
<%@ page import="model.Path" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sky Drive</title>
</head>
<body>
    <%
        List<Path> list = (List<Path>) session.getAttribute("list");
        for (Path path : list) {
            if (path.getType().equals("f")) {
                out.println(
                    "<form action=\"download\" method=\"post\">" +
                    "<input type=\"hidden\" value=\"" + path.getPath() + "\" name=\"path\"/>" +
                    path.getPath() + ":<input type=\"submit\" value=\"Download\"/>" +
                    "</form>"
                );
                out.println(
                    "<form action=\"rmf\" method=\"post\">" +
                    "<input type=\"hidden\" value=\"" + path.getPath() + "\" name=\"file\"/>" +
                    "<input type=\"submit\" value=\"Remove\"/>" +
                    "</form>"
                );
            }
            else {
                out.println(
                    "<form action=\"cd\" method=\"post\">" +
                    "<input type=\"hidden\" value=\"" + path.getPath() + "\" name=\"path\"/>" +
                    path.getPath() + ":<input type=\"submit\" value=\"Enter\"/>" +
                    "</form>"
                );
                out.println(
                    "<form action=\"rmd\" method=\"post\">" +
                    "<input type=\"hidden\" value=\"" + path.getPath() + "\" name=\"dir\"/>" +
                    "<input type=\"submit\" value=\"Remove\"/>" +
                    "</form>"
                );
            }
        }
        out.println("--------------------------------------------------------------------------------------------------");
    %>
    <form action="/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file"/>
        FileName:<input type="text" name="path"/>
        <input type="submit" value="Upload"/>
    </form>
    <form action="/mkdir" method="post">
        <input type="text" name="dir"/>
        <input type="submit" value="Create New Dir"/>
    </form>
    <form action="/prev" method="post">
        <input type="submit" value="Return"/>
    </form>
    <form action="/signOut" method="post">
        <input type="submit" value="SignOut"/>
    </form>
</body>
</html>
