<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<?php
    $cookie_name = "ttuchatid";
    setcookie($cookie_name, "", time() - 3600);
    
    if( isset($_POST['Login']) ){
                $loginid = $_POST["ID"];
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
                                    WHERE $loginid = ID";

                    $result = mysqli_query($conn, $sql);

                    if (mysqli_num_rows($result) == 1) {
                            $row = mysqli_fetch_assoc($result);
                            echo "ID: " .$row["ID"]. " - NickName: " . $row["NickName"]."<br>";

                            setcookie($cookie_name, $loginid, time() + (86400 * 30), "/"); // 86400 = 1 day
                            header("location: chatroom.php");
                    }

                    else if (mysqli_num_rows($result) == 0) {
                            echo "No ID". $loginid . "<br>";
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
    </head>
    <body>
        
        <form action="" method="POST">
            ID : <input type="number" name="ID" required="required">
            <input type="submit" name="Login" value="Login">
        </form>
        
    </body>
</html>
