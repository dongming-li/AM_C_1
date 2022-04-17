<?php
include('dbConnection.php');

error_reporting(-1);
ini_set('display_errors','On');

$guildName = $_GET["guildName"];
$userID = $_GET["userID"];

addNewGuild($guildName, $userID);

function addNewGuild($guildName, $userID){
    global $con;
    $query = "Select * from guild where guildName = $guildName";
    
    $result = mysqli_query($con, $query);

    if($result->num_rows != 0){
        $data = ['result' => 0];
        echo json_encode($data);
    }
    else{
        $query = "insert into guild (guildName) values ($guildName)";
        $con->query($query);

        $rows = array();

        $query = "select guildID from guild where guildName = $guildName";
        $result = mysqli_query($con, $query);
        while($row = $result->fetch_array(MYSQL_BOTH)){
            $rows[] = $row;
        }

        $guildID = $rows[0]['guildID'];
        
        $query = "insert into guildMember (guildID, userID) values ($guildID, $userID)";
        $con->query($query);

        if($con->affected_rows == 1){
            $data = ['result' => 1];
            echo json_encode($data);
        }
    }

}
?>