/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 *
 * @author User
 */
public class Url2Base64 extends HttpServlet {
    
    //public static final String MONGODB_HOST = "192.168.1.135";
    public static final String MONGODB_HOST = "172.17.0.1";
    public static final int MONGODB_PORT = 27017;
    public static final String MONGODB_DATEBASE = "SSR";
    
    private static final MongoClient mongoClient;
    private static final MongoDatabase mongoDatabase;  
    
    private static final Base64.Encoder encoder = Base64.getEncoder();
    
    static {
        mongoClient = new MongoClient( MONGODB_HOST , MONGODB_PORT );
        mongoDatabase = mongoClient.getDatabase(MONGODB_DATEBASE);  
    }
    
    private static String toBase64 (String str) throws UnsupportedEncodingException {
        if (str.length() == 0)
            return "";
        
        String b64 = encoder.encodeToString(str.getBytes("UTF-8"));
        while (b64.charAt(b64.length()-1) == '='){
            b64 = b64.substring(0, b64.length()-1);
        }
        return b64;
    }
    
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
        
        String name = request.getParameter("name");
        if (name == null)
            return;
        
        MongoCollection<Document> collection = mongoDatabase.getCollection("url");
        
        Document urlDoc = collection.find(new Document("name", name)).first();
        if (urlDoc == null)
            return;
        
        String host = urlDoc.getString("host");
        StringBuilder url = new StringBuilder();
        
        for (Document doc : (List<Document>)urlDoc.get("url")) {
            StringBuilder content = new StringBuilder();
            
            content.append(host).append(":")
                .append(doc.getString("port")).append(":")
                .append(doc.getString("protocol")).append(":")
                .append(doc.getString("method")).append(":")
                .append(doc.getString("obfs")).append(":")
                .append(toBase64(doc.getString("password"))).append("/?");
            
            Document params = (Document)doc.get("params");
            
            for (String key : params.keySet()) {
                String data = toBase64(params.getString(key));
                content.append(key).append("=").append(data).append("&");
            }
            content.deleteCharAt(content.length()-1);
            
            url.append("ssr://")
                .append(toBase64(content.toString()))
                .append("\n");
        }
        url.deleteCharAt(url.length()-1);
        
        try (PrintWriter out = response.getWriter()) {
            out.print(toBase64(url.toString()));
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
        processRequest(request, response);
    }

}
