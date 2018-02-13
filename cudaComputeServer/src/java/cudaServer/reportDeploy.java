/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cudaServer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import miningWorker.Deployer;
import miningWorker.Worker;

/**
 *
 * @author Jasper
 */
@WebServlet(name = "reportDeploy", urlPatterns = {"/reportDeploy"})
public class reportDeploy extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (Worker.shutdownWorker)
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        else if (Worker.killWorker)
            response.setStatus(HttpServletResponse.SC_CREATED);
        else
            response.setStatus(HttpServletResponse.SC_OK);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ip = request.getParameter("i");
        String content  = request.getParameter("content");
        
        System.out.println(ip);
        System.out.println(content);
        
        if (ip != null && content != null)
            Deployer.insertLogByIp(ip, content);
        
        processRequest(request, response);
    }

}
