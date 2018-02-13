<%-- 
    Document   : ipDetail
    Created on : 2017/9/8, 上午 01:07:16
    Author     : Jasper
--%>

<%@page import="java.util.List"%>
<%@page import="miningWorker.Worker"%>

<%
    String ip = request.getParameter("ip");
    String day = request.getParameter("day");
    
    if (ip == null)
        return;
    
    if (day == null)
        day = "0";
    
    Worker worker = Worker.getWorkerByIp(ip);
    
    if (worker == null)
        return;
    
    List<String> logList = worker.getWorkerLogs(day);
    
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=ip%> log detail</title>
        
        <style>
            body{
                text-align: center;
            }
            
            table {
                margin:0 auto;
            }

            td {
                text-align: left;
                padding:7px;
                border:1px solid #828282;
                border-left:hidden;
                border-right:hidden;
                border-top:hidden;
                font-size:15px;
                font-family:Microsoft JhengHei;
            }
        </style>
        
    </head>
    <body>
        <h1><%=ip%> log detail</h1>
        
        <div>
            <%  for (int i = 0; i <= 5; i++) {%>
                <a href="ipDetail.jsp?ip=<%=ip%>&day=<%=i%>"><%=i%></a> &nbsp
            <% } %>
        </div>
        
        <table>
        <%  for (String log : logList) {%>
                <tr>
                    <td><%=log%></td>
                </tr>
        <% } %>
        </table>
    </body>
</html>
