<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign In</title>
</head>
<body>
    <form action="signIn" method="post">
        Username:<input type="text" name="username"/>
        Password:<input type="password" name="password"/>
        <input type="submit" value="Submit"/>
    </form>

    <form action="signUp" method="get">
        <input type="submit" value="SignUp">
    </form>
</body>
</html>
