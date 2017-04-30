/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelt;

import java.io.IOException;
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
@WebServlet(name = "MainPage", urlPatterns = {"/main"})
public class MainPage extends HttpServlet {

    ClientModel client = new ClientModel();
    
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
        response.setContentType("text/html");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(client.getLoginPage());
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
        
        if(request.getCookies() != null && request.getParameter("action") != null) {
            if (!request.getCookies()[0].getName().equals("user")){
                processRequest(request, response);
                return;
            }
                
            response.setContentType("text/html");
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            
            String ID = request.getCookies()[0].getValue();
            
            if (request.getParameter("action").equals("search")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getSearchPage());
              }
            }
            else if (request.getParameter("action").equals("upload")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getUploadPages(ID));
              }
            }
            else if (request.getParameter("action").equals("log")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getLogPage(request.getParameter("page")));
              }
            }
            else if (request.getParameter("action").equals("modifyAttr")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getModifyAttrPage());
              }
            }
            else if (request.getParameter("action").equals("newProject")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getNewProjectPage());
              }
            }
            else if (request.getParameter("action").equals("searchProject")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getSearchProjectPage());
              }
            }
            else if (request.getParameter("action").equals("Projectlog")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getProjectLogPage(request.getParameter("page")));
              }
            }
            else if (request.getParameter("action").equals("ModifyCate")) {
              try (PrintWriter out = response.getWriter()) {
                out.println(client.getCatePage());
              }
            }
            return;
        }
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
            response.setContentType("text/html");
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            if ( request.getParameter("ID") != null && request.getParameter("PWD") != null && request.getParameter("Login") != null ){
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.checkLogin(response, request.getParameter("ID"), request.getParameter("PWD")) );
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("getModify") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.getModifyPages(ID, request) );
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("checkModify") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.checkModifyPage(ID, request) );
                }
            }     
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("toModify") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.modifyPages(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("getAttribute") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.getAttrPage(ID , request.getParameter("attr")) );
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("checkModifyAttr") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.checkModifyAttrPage(ID, request) );
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("modifyAttr") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.modifyAttrPage(ID, request) );
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("uploadData") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println( client.uploadData(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("searchAll") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.getSearchResultPages(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("CreateProject") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.getCreateProjectPage(request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("checkModifyProject") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.getCheckProjectPage(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("ModifyProject") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.CreatProject(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("SearchProject") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.getSearchProjectResultPage(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("CheckDeleteProject") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.getCheckDeleteProject(ID, request));
                }
            }
            else if (request.getCookies()[0].getName().equals("user") && request.getParameter("DeleteProject") != null) {
                String ID = request.getCookies()[0].getValue();
                try (PrintWriter out = response.getWriter()) {
                    out.println(client.DeleteProject(ID, request));
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
