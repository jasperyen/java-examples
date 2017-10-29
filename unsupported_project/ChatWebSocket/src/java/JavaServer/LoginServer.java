/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaServer;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jasper-Yen
 */
@WebServlet(name = "LoginServer", urlPatterns = {"/LoginServer"})
public class LoginServer extends HttpServlet {
    
    String DBurl = "jdbc:mysql://127.0.0.1:3306/chat?useUnicode=true&characterEncoding=utf-8";
    String DBuser = "root";
    String DBpassword = "";
    

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
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.println( getLoginWebsite() );
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
        
        if ( request.getParameter("ID") != null && request.getParameter("PWD") != null && request.getParameter("Login") != null ){
            String ID = request.getParameter("ID");
            String PWD = request.getParameter("PWD");
            String NickName;
            
            CheckPassword check = new CheckPassword(ID, PWD);
            
            if ( check.check() ){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    out.println("Can't register JDBC driver ! " + ex.toString());
                }
                try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                    Boolean isNickName = false;
                    Statement stm = conn.createStatement();
                    ResultSet result = stm.executeQuery("SELECT NickName "
                                                                                    + "FROM user "
                                                                                    + "WHERE ID = "+ ID);
                    
                    if (result.next()) {
                        NickName = result.getString("NickName");
                        isNickName = true;
                    }
                    else
                        NickName = check.getStuName();
                    
                    if ( !isNickName ){
                        stm.executeUpdate("INSERT INTO user (ID, NickName) "
                                                            + "VALUES (\'"+ID+"\', \'"+NickName+"\')");
                    }

                    try (PrintWriter out = response.getWriter()) {
                        out.println(getNickNamePage(ID, check.getStuNo(), check.getStuName(), NickName));
                    }
                    
                }catch(SQLException ex){
                    out.println("Select NickName failed ! ");
                    ex.printStackTrace();
                }

            }
            else{
               try (PrintWriter out = response.getWriter()) {
                   out.println("<script language=\"javascript\"> alert('帳號或密碼錯誤 ! '); </script>");
                   out.println( getLoginWebsite() );
                }
            }
        }
        else if ( request.getParameter("ID") != null && request.getParameter("NickName") != null && request.getParameter("StartChat") != null ){
            String ID = request.getParameter("ID");
            String NickName = request.getParameter("NickName");
            try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                Statement stm = conn.createStatement();

                stm.executeUpdate("UPDATE user "
                                                    + "SET NickName = \'"+NickName+"\' "
                                                    + "WHERE ID = \'"+ID+"\' ");
                
                try (PrintWriter out = response.getWriter()) {
                    Cookie cookie = new Cookie("ttuchatid", ID);
                    cookie.setMaxAge(24*60*60);
                    response.addCookie(cookie); 
                   out.println("<script language=\"javascript\"> document.location.href=\"http://localhost/ttuchat/chatroom.php\"; </script>");
                }

            }catch(SQLException ex){
                out.println("Update NickName failed ! ");
                ex.printStackTrace();
            }
        }
        else
            processRequest(request, response);
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

    
    private String getNickNamePage (String ID, String No, String Name, String NickName){
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title></title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <style>\n" +
                    "            #background{\n" +
                    "                position: absolute;\n" +
                    "                top: 0;\n" +
                    "                height : 100vh;\n" +
                    "                width : 100vw;\n" +
                    "                background-image: url('backimg.jpg');\n" +
                    "                background-size: 100% auto;\n" +
                    "                background-position: center;\n" +
                    "                background-repeat: no-repeat;\n" +
                    "            }\n" +
                    "            #block {\n" +
                    "                    margin: 30vh auto;\n" +
                    "                    width: 40vw;\n" +
                    "                    height: 40vh;\n" +
                    "                    text-align: center;\n" +
                    "            }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "<body style = \"overflow-x:hidden; overflow-y:hidden;\">\n" +
                    "            <div id='background'>\n" +
                    "                    <div id='block'>\n" +
                    "                    <span style=\"font-family:cursive;font-size:1cm\">請設定暱稱</span>\n" +
                    "                    <form action=\"\" method=\"POST\">\n" +
                    "                        <table style=\"margin: 0px auto\">\n" +
                    "                            <tr>\n" +
                    "                                    <td>姓名</td>\n" +
                    "                                    <td>"+Name+"</td>\n" +
                    "                            </tr>\n" +
                    "                            <tr>\n" +
                    "                                    <td>班號</td>\n" +
                    "                                    <td>"+No+"</td>\n" +
                    "                            </tr>\n" +
                    "                            <tr>\n" +
                    "                                    <td>暱稱</td>\n" +
                    "                                    <td><input type=\"text\" name=\"NickName\" value=\""+NickName+"\" required=\"required\"></td>\n" +
                    "                            </tr>\n" +
                    "                            </table>\n" +
                    "                        <input type=\"hidden\" name=\"ID\" value=\""+ID+"\">" +
                    "                        <input type=\"submit\" name=\"StartChat\" value=\"開始聊天\">\n" +
                    "                    </form>\n" +
                    "                    </div>\n" +
                    "            </div>\n" +
                    "    </body>\n" +
                    "</html>\n";
    }
    
    private String getLoginWebsite(){
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title></title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <style>\n" +
                    "            #background{\n" +
                    "                position: absolute;\n" +
                    "                top: 0;\n" +
                    "                height : 100vh;\n" +
                    "                width : 100vw;\n" +
                    "                background-image: url('backimg.jpg');\n" +
                    "                background-size: 100% auto;\n" +
                    "                background-position: center;\n" +
                    "                background-repeat: no-repeat;\n" +
                    "            }\n" +
                    "            #block {\n" +
                    "                    margin: 30vh auto;\n" +
                    "                    width: 40vw;\n" +
                    "                    height: 40vh;\n" +
                    "                    text-align: center;\n" +
                    "            }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "<body style = \"overflow-x:hidden; overflow-y:hidden;\">\n" +
                    "            <div id='background'>\n" +
                    "                    <div id='block'>\n" +
                    "                    <span style=\"font-family:cursive;font-size:1.2cm\">Chat Room</span>\n" +
                    "                    <form action=\"\" method=\"POST\">\n" +
                    "                        <table style=\"margin: 0px auto\">\n" +
                    "                            <tr>\n" +
                    "                                    <td>學號</td>\n" +
                    "                                    <td><input type=\"text\" name=\"ID\" required=\"required\"></td>\n" +
                    "                            </tr>\n" +
                    "                            <tr>\n" +
                    "                                    <td>密碼</td>\n" +
                    "                                    <td><input type=\"password\" name=\"PWD\" required=\"required\"></td>\n" +
                    "                            </tr>\n" +
                    "                            </table>\n" +
                    "                        <input type=\"submit\" name=\"Login\" value=\"Login\">\n" +
                    "                    </form>\n" +
                    "                    </div>\n" +
                    "            </div>\n" +
                    "    </body>\n" +
                    "</html>\n" ;
    }
    
}
