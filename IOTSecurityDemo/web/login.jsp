<%-- 
    Document   : login
    Created on : 2018/1/14, 下午 11:54:44
    Author     : Jasper
--%>

<%
    session.setAttribute("user", null);
    
    
    String account = request.getParameter("account");
    String password = request.getParameter("password");
    
    if (request.getMethod().equals("POST") && account != null && password != null) {
        if (account.equals("admin")) {
            if (password.equals("laba5606")) {
                //login success
                session.setAttribute("user", "true");
                %>
                <script>document.location.href="index.jsp";</script>
                <%
            }
            else {
                //wrong password
                %>
                <script>alert("密碼錯誤 ! ");</script>
                <script>document.location.href="";</script>
                <%
            }
        }
        else {
            //wrong account
            %>
            <script>alert("帳號錯誤 ! ");</script>
            <script>document.location.href="";</script>
            <%
        }
        return;
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        
        <style>
            .main{
                margin:0 auto;
                text-align: center;
                width: 100%;
            }
        </style>
    </head>
    <body>
        <div class="main">
            <h1>Login Page</h1>
            
            <form method="POST">

                <table style = "text-align: center; margin:0px auto;">
                    <tr><td>帳號 </td><td><input type=text name='account'></td></tr>
                    <tr><td>密碼 </td><td><input type=password name='password'></td></tr>	
                </table>
                <h4><input type="submit" name="login" value="登入"></h4>

            </form>
        </div>
    </body>
</html>
