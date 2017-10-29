<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->

<?php
    $cookie_name = "ttuchatid";
    if(!isset($_COOKIE[$cookie_name])) {
            echo "Cookie named '" . $cookie_name . "' is not set!";
            header("location: http://localhost:8084/ChatWebSocket/LoginServer");
    }
    else {
//         echo "Cookie " . $cookie_name . " is set!<br>";
//         echo "Value is: " . $_COOKIE[$cookie_name];
            $userid = $_COOKIE[$cookie_name];
            $nickname ="";
            
            $servername = "127.0.0.1";
            $username = "root";
            $password = "";
            $dbname = "chat";
            
            // Create connection
            $conn = mysqli_connect($servername, $username, $password, $dbname);
            // Check connection
            if ($conn) {
                $sql = "SELECT ID ,NickName
                                FROM user
                                WHERE $userid = ID";

                mysqli_set_charset ( $conn , "utf8" );
                
                $result = mysqli_query($conn, $sql);

                if (mysqli_num_rows($result) == 1) {
                        $row = mysqli_fetch_assoc($result);
                        $nickname = $row["NickName"];
                }
                else if (mysqli_num_rows($result) == 0) {
                        header("location: http://localhost:8084/ChatWebSocket/LoginServer");
                }
            }
            else{
                die("Connection failed: " . mysqli_connect_error());
            }
        
    }
?>

<html>
    <head>
        <meta charset="UTF-8">
        <title></title>
        <script src="jquery-2.2.3.min.js" type="text/javascript"></script>
        
        <script>
            var wsURI = "ws://localhost:8084/ChatWebSocket/chatSocket";
            
            function connWS( ID ){
                websocket = new WebSocket(wsURI);                 
                websocket.onopen = function(){
                    alert("conn success ! ");
                    reciveMessage();
                    loginID(ID);
                };
                
                onError();
            }
            
            function reciveMessage(){
                websocket.onmessage = function (evt) {
               //     $("#recordTable").append("<tr><td>"+evt.data+"</td></tr>");
                    
                    var obj = JSON.parse(evt.data);
                    if (obj.System){
                        $("#systemTable").append("<tr><td>"+obj.System+"</td></tr>");
                    }
                    else if (obj.newRecord){
                        var record  = obj.newRecord;
                        $("#recordTable").append("<tr><td>"+record.NickName+" : </td><td>"+record.Data+"</td><td>"+record.Time+"</td></tr>");
                    }
                    else if (obj.userList){
                        $("#userTable").empty();
                        var list  = obj.userList;
                        for(var i = 0; i < list.length; i++){
                            $("#userTable").append("<tr><td>"+list[i]+"</td></tr>");
                        }
                    }
                    else if (obj.oldRecord){
                        var recordarray = obj.oldRecord;
                        if (recordarray.length == 0)
                            $("#recordTable").prepend("<tr><td>No record ! </td></tr>");
                            
                        else
                            for (var i = 0; i < recordarray.length; i++){
                                var record = recordarray[i];
                                $("#recordTable").prepend("<tr><td>"+record.NickName+" : </td><td>"+record.Data+"</td><td>"+record.Time+"</td></tr>");
                            }
                    }
                    else{
                        $("#systemTable").append("<tr><td>"+evt.data+"</td></tr>");
                    }
                };
            }
            
            function onError(){
                websocket.onerror  = function (evt) {
                    alert("onError : " + evt.data);
                };
            }
            
            function loginID (ID) {
                var obj = {
                    ID : ID
                };
                var jsondata = JSON.stringify(obj);
                websocket.send(jsondata);
                //alert("send : " + jsondata);
                
                inRoom("0000");
            }
            
            function inRoom( RoomNo ){
                $("#recordTable").empty();
                var obj = {
                    RoomNo : RoomNo
                };
                var jsondata = JSON.stringify(obj);
                websocket.send(jsondata);
                //alert("send : " + jsondata);
            }
            
            function sendData( formdata ){
                if (formdata.txt.value == "")
                    return;
                
                var obj = {
                    Type : formdata.type.value,
                    Data : formdata.txt.value
                };
                var jsondata = JSON.stringify(obj);
                websocket.send(jsondata);
                //alert("send : " + jsondata);
                
                formdata.txt.value = "";
            }
            
            function showRecord(){
                var obj = {
                    requestTime : "1"
                };
                var jsondata = JSON.stringify(obj);
                websocket.send(jsondata);
                //alert("send : " + jsondata);
                
                
            }

           connWS( <?php echo $userid; ?> );
        </script>
        
        <style>
            #header {
                    position : absolute;
                    background-color:black;
                    top : 0;
                    right : 0;
                    left : 0;
                    height: 70px;
                    color:white;
                    text-align:center;
                    padding:5px;
                    z-index:2;
            }
            #nav {
                    position : absolute;
                    line-height:30px;
                    background-color:#D3D3D3;
                    top : 80px;
                    left : 0;
                    right : 92vw;
                    bottom : 0;
                    width:8vw;
                    padding:10px;	      
                    z-index:1;
            }
            #section {
                    background-color:#F0F8FF;
                    position : absolute;
                    top : 80px;
                    left : 8vw;
                    right : 20vw;
                    bottom : 45px;
                    text-align:center;
                    float:left;
                    padding:5px;	
                    z-index:0;
                    overflow-y:auto;
            }
            #upperRight {
                    background-color:#F8F8F8 ;
                    position : absolute;
                    top : 80px;
                    left : 80vw;
                    right : 0;
                    bottom : 40vh;
                    text-align:center;
                    float:left;
                    padding:5px;	
                    z-index:0;
                    overflow-y:auto;
            }
            #lowerRight {
                    background-color:#F8F8F8 ;
                    position : absolute;
                    top : 60vh;
                    left : 80vw;
                    right : 0;
                    bottom : 45px;
                    text-align:center;
                    float:left;
                    padding:5px;	
                    z-index:0;
                    overflow-y:auto;
            }
            #footer {
                    position : absolute;
                    background-color:#eeeeee;
                    bottom : 0;
                    right : 0;
                    left : 8vw;
                    height: 40px;
                    text-align:center;
                    padding:5px;	 	 
                    z-index:0;
            }
    </style>
    </head>
    <body>

        <div id="header">
                <h1>Chat Room Demo</h1>
        </div>

        <div id="nav">
            <form>
                <input type='button' name='room' value= '大廳' onclick='inRoom(0000)'></input><br>
                <?php
                    $conn = mysqli_connect($servername, $username, $password, $dbname);
                    // Check connection
                    if (!$conn) {
                        die("Connection failed: " . mysqli_connect_error());
                    }
                    else{
                        $sql = "SELECT chat_room.RoomNo, chat_room.RoomName
                                        FROM purview, chat_room
                                        WHERE $userid = purview.ID AND 
                                        purview.RoomNo = chat_room.RoomNo";
                        
                        
                        mysqli_set_charset ( $conn , "utf8" );
                      
                        $result = mysqli_query($conn, $sql);

                        if (mysqli_num_rows($result) > 0) {
                                while($row = mysqli_fetch_assoc($result)) {
                                    echo "<input type='button' name='room' onclick='inRoom(" . $row["RoomNo"] . ")' value= '" . $row["RoomName"] . "'></input><br>";
                                }
                        }
                        else  {
          //                      echo "0 results";
                        }
                    }
                ?>
            </form>
        </div>

        <div id="section">
            <button type="button" onclick="showRecord()">顯示紀錄</button>
            
            <table id = "recordTable" style="width:90%">
            </table>
        </div>
        
        <div id="upperRight">
            <table id = "userTable" style="width:90%">
            </table>
        </div>
        
        <div id="lowerRight">
            <table id = "systemTable" style="width:90%">
            </table>
        </div>

        <div id="footer">
            <form>
                <textarea name="txt" rows ="2" cols = "40" required="required"></textarea>
                <input type="hidden" name="type" value="text">
                <input type="button" name="postText" value="送出" onclick="sendData( this.form )" style = "position: relative; top : -14px">
            </form>
        </div>
        
    </body>
</html>
