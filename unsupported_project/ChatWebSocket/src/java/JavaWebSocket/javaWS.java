/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaWebSocket;

import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.*;

/**
 *
 * @author Jasper-Yen
 */
@ServerEndpoint("/chatSocket")
public class javaWS {
    
    static Set userSet = new HashSet<UserData>();

    UserData user;
    String lastRoom;
    
    String DBurl = "jdbc:mysql://127.0.0.1:3306/chat?useUnicode=true&characterEncoding=utf-8";
    String DBuser = "root";
    String DBpassword = "";

    public javaWS() {
        user = new UserData();
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            out.println("Can't register JDBC driver ! " + ex.toString());
        }
        
    }
    
    JSONObject packageRecord (String NickName, String Time, String Type, String Data){
    
        String recordData = "{ \"NickName\" : "+NickName+", \"Time\" : \""+Time
                                                            +"\", \"Data\" : \""+Data+"\", \"Type\" : \""+Type+"\" }";
        
        return new JSONObject (recordData);
    }
    
    static void pushMessage (JSONObject jsonData, String RoomNo) {
    
        try {
            for (Object alluser : userSet){
                UserData userdata = ( (UserData)alluser );
                if (userdata.getRoomNo().equals(RoomNo))
                    userdata.getSession().getBasicRemote().sendText(jsonData.toString());
            }
        }
        catch(Exception ex){
            out.println("pushMessage falied (Exception) : " + ex.toString());
        }
        
    }
    
    static void pushUserList (String RoomNo)  {
        try {
            
           JSONArray array = new JSONArray ();
            for (Object alluser : userSet){
                UserData userdata = ( (UserData)alluser );
                if (userdata.getRoomNo().equals(RoomNo))
                    if (userdata.getNickName() != null)
                        array.put(userdata.getNickName());
            }
            

            JSONObject jsonData = new JSONObject("{ \"userList\" : "+array.toString()+" }" );
            
            //out.println(jsonData.toString());
            
            for (Object alluser : userSet){
                UserData userdata = ( (UserData)alluser );
                if (userdata.getRoomNo().equals(RoomNo))
                    userdata.getSession().getBasicRemote().sendText(jsonData.toString());
            }
        }
        catch(Exception ex){
            out.println("pushUserList falied (Exception) : " + ex.toString());
        }
        
    }
    
    @OnOpen
    public void LoginChat(Session session){
        
        user.setSession(session);
        out.println("Session ID : " + session.getId() + " is login !");
        userSet.add(user);
        
    }
    
    @OnMessage
    public String handleMessage(String message, Session session){
        try {
            
            JSONObject root = new JSONObject(message);
            
            if (root.has("ID")){
                user.setID(root.get("ID").toString());
                
                try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                    Statement stm = conn.createStatement();
                    ResultSet result = stm.executeQuery("SELECT NickName "
                                                                                    + "FROM user "
                                                                                    + "WHERE ID = "+root.get("ID").toString());
                    
                    if (result.next()) {
                        user.setNickName(result.getString("NickName"));
                    }
                    
                }catch(SQLException ex){
                    out.println("Select NickName failed ! ");
                    ex.printStackTrace();
                }
                
                
                out.println("Session ID " + session.getId() + " - Set ID : " + user.getID() + "NickName is " + user.getNickName());
                String log =  "Your Login ID is : " + user.getID();
                return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
            }
            
            else if(root.has("RoomNo")){
                if (user.getID()==null){
                    String log =  "You haven't login !";
                    return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
                }
                
                 try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                    Statement stm = conn.createStatement();
                    ResultSet result = stm.executeQuery("SELECT MAX(RecordID) "
                                                                                    + "FROM record "
                                                                                    + "WHERE RoomNo = 0"+ root.get("RoomNo"));
                    
                    if (result.next()) {
                        user.setRecordID(result.getInt("MAX(RecordID)"));
                    }

                }catch(SQLException ex){
                    out.println("Select RecordID failed ! ");
                    ex.printStackTrace();
                }
                
                if(user.getRoomNo() == null){
                    user.setRoomNo(root.get("RoomNo").toString());
                }
                else{
                    lastRoom = user.getRoomNo();
                    user.setRoomNo(root.get("RoomNo").toString());
                    pushUserList(lastRoom);
                }
                
                out.println("Session ID " + session.getId() + " - Set RoomNo : " + user.getRoomNo() + " - Set RecordID : " + user.getRecordID());
                
                
                pushUserList(user.getRoomNo());
                
                String log =  "You are now in RoomNo : " + user.getRoomNo();
                return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
            }
            
            else if(root.has("Type") && root.has("Data")){
                if (user.getRoomNo()==null || user.getID()==null){
                    String log =  "You haven't login or you are not in any room !";
                    return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
                }
                
                String data = root.get("Data").toString();
                String type = root.get("Type").toString();
                String recordTime = LocalDate.now().toString() + " " + LocalTime.now().getHour() + ":" 
                                    + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond();
                out.println("Session ID " + session.getId() + " - Send Data : " + data +" Type : " + type + " Time : " + recordTime);
                
                
                JSONObject jsonRecord =packageRecord(user.getNickName(), recordTime, type, data);
                JSONObject jsonData = new JSONObject( 
                                                                        "{ \"newRecord\" : "+jsonRecord.toString()+" }" );
                pushMessage(jsonData, user.getRoomNo());
                
                try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                    Statement stm = conn.createStatement();
                    
                    stm.executeUpdate("INSERT INTO record (RoomNo, Time, ID, Type, Data) "
                                                        + "VALUES (\'"+user.getRoomNo()+"\', \'"+recordTime+"\', \'"+user.getID()+"\', \'"+type+"\', \'"+data+"\')");
                    
                }catch(SQLException ex){
                    out.println("Insert record failed ! ");
                    ex.printStackTrace();
                }
                
                return "";
            }
            
            else if(root.has("requestTime")){
                if (user.getRoomNo()==null || user.getID()==null){
                    String log =  "You haven't login or you are not in any room !";
                    return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
                }
                
                JSONArray jsonarray = new JSONArray ();
                
                try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                    
                    Statement stm = conn.createStatement();
                    ResultSet result = stm.executeQuery("SELECT user.NickName, record.Time, record.Type, record.Data, record.RecordID "
                                                                                   + "FROM record, user "
                                                                                   + "WHERE record.RoomNo = "+user.getRoomNo()+" AND "
                                                                                   + "record.RecordID <= "+user.getRecordID()+" AND "
                                                                                   + "record.ID = user.ID "
                                                                                   + "ORDER BY record.RecordID DESC "
                                                                                   + "LIMIT " + root.get("requestTime").toString() );
                    
                    
                    while(result.next()){
                        user.setRecordID(result.getInt("record.RecordID") - 1 );
                        out.println("RecordID : " + result.getInt("record.RecordID"));
                        JSONObject ajson = packageRecord( result.getString("NickName"), result.getDate("Time") + " " + result.getTime("Time"),
                                                                                                result.getString("Type"), result.getString("Data"));
                        jsonarray.put(ajson);
                    }
                    
                    
                    
                }catch(SQLException ex){
                    out.println("Select Record failed ! ");
                    ex.printStackTrace();
                }
                
                 out.println("Session ID " + session.getId() + " - Request Record : " + root.get("requestTime").toString());
                 return new JSONObject("{ \"oldRecord\" : "+jsonarray.toString()+" }" ).toString();
            }
            
            else{
                out.println("Unknow message by session ID  " + session.getId() + " : " + root.toString());
                
                String log =  "I don't know what you request !";
                return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
            }
            

        } catch (Exception ex) {
            ex.printStackTrace();
            
            String log =  "There are something wrong in server :( \n" + ex.toString();
            return new JSONObject("{ \"System\" : \""+log+"\" }" ).toString();
        }

    }
        
    @OnError
    public void isError(Throwable t) {
        out.println("Is Error : " + t.toString());
    }
    
    @OnClose
    public void toclose(Session session) {
        userSet.remove(user);
        pushUserList(user.getRoomNo());
        out.println("Session ID : " + session.getId() + " is close !");
    }
    
}
