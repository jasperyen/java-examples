/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelt;

import DataBaseInfo.DataBaseConn;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONObject;

/**
 *
 * @author Jasper-Yen
 */
@WebServlet(name = "UploadServer", urlPatterns = {"/upload"})
@MultipartConfig(location="C:\\workspace\\usr\\tmp", fileSizeThreshold=1024*1024, 
                                    maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class UploadServer extends HttpServlet implements DataBaseConn{
    private String filepath = "C:\\workspace\\usr\\";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("In wrong place");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");        
        if ( request.getPart("ExcelFile") != null && request.getPart("ID") != null){
            String ID = "";
            try( BufferedReader in =  new BufferedReader ( new InputStreamReader( request.getPart("ID").getInputStream() , "utf-8") ) ){
                String buff;
                while ( (buff = in.readLine()) != null ){
                    ID = ID + buff;
                }
            }
            
            
            Part reqPart = request.getPart("ExcelFile");
            String header = reqPart.getHeader("Content-Disposition");
            String filename = ID + "_" + header.substring(
                    header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
            

            try( BufferedInputStream in = new BufferedInputStream ( reqPart.getInputStream() )
                    ; OutputStream out = new FileOutputStream( filepath + filename) ){
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
            }
            
            ExcelReader eReader = new ExcelReader(new File(filepath + filename));
            try (PrintWriter out = response.getWriter()) {
                out.println( eReader.ComponentsToHTML() );
            }
            
            new File(filepath + filename).delete();
        }
        else{
            try (PrintWriter out = response.getWriter()) {
                String log = "Dosen't request FileData ! ";
                out.println(log);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
}
