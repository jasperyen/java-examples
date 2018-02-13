/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cudaServer;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import miningWorker.Worker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
@WebServlet(name = "reportWorkState", urlPatterns = {"/reportWorkState"})
public class reportWorkState extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        JSONObject json = new JSONObject();
        JSONArray jlist = new JSONArray();
        List<String> workerList = Worker.getDeathWorker();
        
        workerList.forEach( w -> {
            jlist.put(w);
        });
        
        json.put("DeathWorkers", jlist);
        
        response.getWriter().write(json.toString());
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
