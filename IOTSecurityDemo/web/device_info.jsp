<%-- 
    Document   : device_info
    Created on : 2018/1/16, 上午 12:18:41
    Author     : Jasper
--%>

<%@page import="java.util.Collections"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="MQTT.IOTDevice.Stamp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="MQTT.IOTDevice"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String name = request.getParameter("device_name");
    IOTDevice device = null;
    
    if (name != null ) {
        device = IOTDevice.getDeviceByName(name);
    }
    
    if (device == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    if (request.getParameter("change") != null) {
        device.maxTemperature = Double.valueOf(request.getParameter("maxTemperature"));
        device.minTemperature = Double.valueOf(request.getParameter("minTemperature"));
        %>
        <script>document.location.href="";</script>
        <%
        return;
    }
    
    double maxTemperature = device.maxTemperature;
    double minTemperature = device.minTemperature;
    String AESKey = device.AESKey;
    String deviceUid = device.deviceUid;
    
    List<String> logList = new ArrayList<>();
    
    for (Stamp stamp : device.getDeviceLog()) {
        String onoff = stamp.state ? "ON" : "OFF";
        
        String line = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(stamp.time) + 
                        " / " + onoff + " / 溫度: " + stamp.temperature +
                        " / 設定上限: " + stamp.maxTemperature + " / 設定下限: " + stamp.minTemperature;
        logList.add(line);
    }
    
    Collections.reverse(logList);
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=name%></title>
        
        <style>
            .main{
                margin:0 auto;
                text-align: center;
                width: 100%;
                font-size:16px;
                font-family:Microsoft JhengHei;
            }
            
            .logs {
                margin:0 auto;
            }

            .lines {
                text-align: left;
                padding:7px;
                border:1px solid #828282;
                border-left:hidden;
                border-right:hidden;
                border-top:hidden;
            }
        </style>
        
    </head>
    <body>
        <div class="main">
            <h1><%=name%> logs</h1>
            
            <form method="POST">
                <input type="hidden" name="device_name" value="<%=name%>">
                
                <table style = "text-align: left; margin:0px auto;">
                    <tr><td>UID :</td><td><%=deviceUid%></td></tr>
                    <tr><td>AES Key :</td><td><%=AESKey%></td></tr>
                </table>
                
                <br><br>
                
                <table style = "text-align: center; margin:0px auto;">
                    <tr><td>溫度上限 <input type=number name='maxTemperature' value='<%=maxTemperature%>' required></td></tr>
                    <tr><td>溫度下限 <input type=number name='minTemperature' value='<%=minTemperature%>' required></td></tr>
                </table>
                <h4><input type="submit" name="change" value="更改"></h4>
            </form>
            
            <br>
            
            <table div class="logs">
            <%  for (String log : logList) {%>
                    <tr>
                        <td div class="lines"><%=log%></td>
                    </tr>
            <% } %>
            </table>
        </div>
    </body>
</html>
