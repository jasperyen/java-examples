
package JavaWebSocket;

import javax.websocket.Session;


public class UserData {
    
    private Session session;
    private String ID, RoomNo, NickName;
    private int RecordID;

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int RecordID) {
        this.RecordID = RecordID;
    }


    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }
    
    public UserData() {
    }


    
    public void setSession(Session session) {
        this.session = session;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setRoomNo(String RoomNo) {
        this.RoomNo = RoomNo;
    }
    


    public Session getSession() {
        return session;
    }

    public String getID() {
        return ID;
    }

    public String getRoomNo() {
        return RoomNo;
    }
    
}
