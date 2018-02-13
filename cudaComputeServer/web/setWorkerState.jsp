<%-- 
    Document   : setWorkerState
    Created on : 2017/9/8, 下午 04:57:56
    Author     : Jasper
--%>

<%@page import="miningWorker.Worker"%>
<%
    if (request.getMethod().equals("POST")){
        if (request.getParameter("toKill") != null)
            Worker.killWorker = true;
        else
            Worker.killWorker = false;

        if (request.getParameter("toShutDown") != null)
            Worker.shutdownWorker = true;
        else
            Worker.shutdownWorker = false;
        
        response.sendRedirect("totalWorker.jsp");
        return;
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set Worker State</title>
        <style>
            body{
                text-align: center;
            }
            
            form {
                margin:0 auto;
            }
        </style>
        
    </head>
    <body>
        <h1>Set Worker State</h1>
        <form method="POST">
            <p>Kill worker : <input type="checkbox" name="toKill" <%if (Worker.killWorker) {%>checked<%}%> ></p>
            <p>Shutdown worker : <input type="checkbox" name="toShutDown" <%if (Worker.shutdownWorker) {%>checked<%}%> ></p>
            <input type="submit" name="submit" value="submit">
        </form>
    </body>
</html>
