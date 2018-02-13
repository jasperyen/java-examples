<%-- 
    Document   : deployDetail
    Created on : 2017/12/2, 下午 08:59:31
    Author     : Jasper
--%>

<%@page import="miningWorker.Deployer"%>
<%@page import="java.util.List"%>

<%
    String ip = request.getParameter("ip");
    
    if (ip == null)
        return;
    
    Deployer deployer = Deployer.getDeployerByIp(ip);
    
    if (deployer == null)
        return;
    
    List<String> logList = deployer.getHTLMLogList();
    
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=ip%> Deployer detail</title>
        
        <style>
            body{
                text-align: center;
            }
            
            table {
                margin:0 auto;
            }

            td {
                width: 55em;
                text-align: left;
                padding:7px;
                border:1px solid #828282;
                border-left:hidden;
                border-right:hidden;
                border-top:hidden;
                font-size:15px;
                line-height: 26px;
                font-family:Microsoft JhengHei;
            }
        </style>
        
    </head>
    <body>
        <h1><%=ip%> Deployer detail</h1>
        
        <table>
        <%  for (String log : logList) {%>
                <tr>
                    <td><%=log%></td>
                </tr>
        <% } %>
        </table>
    </body>
</html>
