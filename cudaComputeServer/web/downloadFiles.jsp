<%-- 
    Document   : downloadFiles
    Created on : 2017/10/31, 下午 10:25:00
    Author     : Jasper
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="cudaServer.initServer"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.IOException"%>

<%
    String type = request.getParameter("type");
    
    if (type == null)
        return;
    
    Map<String, String> fileMap = initServer.getFileMap();
    
    if (!fileMap.containsKey(type))
        return;
    
    String filename = fileMap.get(type);

    response.setContentType("APPLICATION/OCTET-STREAM");   
    response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");

    try (FileInputStream fileInputStream = new FileInputStream(initServer.getDOWNLOAD_PATH() + filename); 
            ServletOutputStream os = response.getOutputStream()) {
        
        int read=0;
        byte[] bytes = new byte[1024];
        
        while((read = fileInputStream.read(bytes))!= -1){
            os.write(bytes, 0, read);
        }
    } catch (IOException ex) {
        System.out.println("In IOException : " + ex);
    }
%>