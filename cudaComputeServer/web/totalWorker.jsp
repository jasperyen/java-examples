<%-- 
    Document   : totalWorker
    Created on : 2017/9/8, 下午 04:08:52
    Author     : Jasper
--%>

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List"%>
<%@page import="miningWorker.Worker"%>
<%
    List<Entry<String, String>> workers = Worker.getTotalWorkerLog();
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Total workers</title>
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
        <h1>Total workers</h1>
        
        <p><a href="setWorkerState.jsp">Set worker state</a></p>
        
        <table>
        <%  for (Entry<String, String> worker : workers) {%>
                <tr>
                    <td>
                        <a href="ipDetail.jsp?ip=<%=worker.getKey()%>"><%=worker.getKey()%></a>&nbsp - &nbsp<%=worker.getValue()%>
                    </td>
                </tr>
                <%
            }   
        %>
        </table>
    </body>
</html>
