/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelt;

import DataBaseInfo.DataBaseName.db;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jasper-Yen
 */
@WebServlet(name = "DownloadServer", urlPatterns = {"/download"})
public class DownloadServer extends HttpServlet {
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
        
        if ( request.getParameter("FileName") == null ){
            processRequest(request, response);
            return;
        }
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        
        if ( request.getParameter("FileName").equals("getTemplate") ) {
            ExcelWriter ex = new ExcelWriter();
            
            File file = new File( ex.getFilepath() + ex.getFilename() );
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + ex.getFilename());

            try( BufferedOutputStream out = new BufferedOutputStream( response.getOutputStream() ) ;
                     InputStream in = new FileInputStream(file)){
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
             }
            
            file.delete();
        }
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
        
        if (request.getCookies()[0].getName().equals("user") && request.getParameter("getExcel") != null) {
            String ID = request.getCookies()[0].getValue();
            
            ExcelWriter ex = new ExcelWriter(request);
            
            File file = new File( ex.getFilepath() + ex.getFilename() );
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + ex.getFilename());

            try( BufferedOutputStream out = new BufferedOutputStream( response.getOutputStream() ) ;
                     InputStream in = new FileInputStream(file)){
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
             }
            
            file.delete();
        }
        
        
        if (request.getCookies()[0].getName().equals("user") && request.getParameter("getProject") != null) {
            String ID = request.getCookies()[0].getValue();
            
            ExcelWriter ex = new ExcelWriter( request.getParameter("ProjectName"), ID);
            
            File file = new File( ex.getFilepath() + ex.getFilename() );
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + ex.getFilename());

            try( BufferedOutputStream out = new BufferedOutputStream( response.getOutputStream() ) ;
                     InputStream in = new FileInputStream(file)){
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
             }
            
            //file.delete();
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
