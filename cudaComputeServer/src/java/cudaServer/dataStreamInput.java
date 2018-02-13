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
import miningWorker.Worker;


@WebServlet(name = "dataStreamInput", urlPatterns = {"/dataStreamInput"})
public class dataStreamInput extends HttpServlet {


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
        
        request.setCharacterEncoding("UTF-8");
        
        String ip = request.getParameter("i");
        String state = request.getParameter("s");
        
        //System.out.println(ip);
        //System.out.println(state);
        
        if (ip != null && state != null)
            Worker.insertWorkerLog(ip, state);
        
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
