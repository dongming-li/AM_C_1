<?php
error_reporting(-1);
ini_set('display_errors','On');

include('dbConnection.php');

$userName = $_GET['userName'];

guildInfo($userName);

function guildInfo($userName){
    global $con;
    $query = "select guildName,g.guildID From db309amc1.guild g
              inner join db309amc1.guildMember m
                  on g.guildID = m.guildID
              inner join db309amc1.user u
              on u.userId = m.userID
              where u.userName = $userName";

    $result = mysqli_query($con, $query);

    $rows = array();
    while($r = mysqli_fetch_assoc($result)) {
        $rows[] = $r;
    }

    echo json_encode($rows);

    $con->close();
}
?>