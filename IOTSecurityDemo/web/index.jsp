<%-- 
    Document   : index
    Created on : 2018/1/14, 下午 11:55:20
    Author     : Jasper
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="MQTT.IOTDevice"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<String> deviceNameList = IOTDevice.getDeviceNameList();
    
    JSONArray series = new JSONArray();
    
    for (String name : deviceNameList) {
        IOTDevice device = IOTDevice.getDeviceByName(name);
        
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("url", "device_info.jsp?device_name=" + name);
        json.put("connectNulls", true);
        json.put("data", device.getTemperatureHistory());
        
        series.put(json);
    }
    
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Total device</title>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://code.highcharts.com/highcharts.src.js"></script>
        
        <script language="JavaScript">
            $(document).ready(function() {
               var title = {
                   text: '裝置環境溫度'   
               };
               var xAxis = {
                   categories: ['19 min', '18 min', '17 min', '16 min' ,'15 min',
                                '14 min', '13 min', '12 min', '11 min' ,'10 min',
                                '9 min', '8 min', '7 min', '6 min' ,'5 min',
                                '4 min', '3 min', '2 min', '1 min', 'current']
               };
               var yAxis = {
                  title: {
                     text: 'Temperature (\xB0C)'
                  },
                  plotLines: [{
                     value: 0,
                     width: 1,
                     color: '#808080'
                  }]
               };   

               var tooltip = {
                  valueSuffix: '\xB0C'
               };

               var legend = {
                  layout: 'vertical',
                  align: 'right',
                  verticalAlign: 'middle',
                  borderWidth: 0
               };

               var plotOptions = {
                    series: {
                        point: {
                            events: {
                                click: function(){
                                   window.open(this.series.userOptions.url);
                                }
                            }
                        }
                    }
                };

               var series = <%=series%>;
              
               var json = {};

               json.title = title;
               json.xAxis = xAxis;
               json.yAxis = yAxis;
               json.tooltip = tooltip;
               json.legend = legend;
               json.plotOptions = plotOptions;
               json.series = series;

               $('#container').highcharts(json);
            });
    </script>
        
    </head>
    <body>
        
        <div id="container" style="width: 1050px; height: 600px; margin: 0 auto"></div>
        
    </body>
</html>
