<?php
    error_reporting(-1);
ini_set('display_errors','On');

include('dbConnection.php');

$userName = $_GET['userName'];
$guildID = $_GET['guildID'];

getGuildMembers($userName, $guildID);

function getGuildMembers($userName, $guildID){
    global $con;
    $query = "select * from db309amc1.user where user.userName like '%$userName%'
    and user.userID not in (select guildMember.userID from db309amc1.guildMember where guildMember.guildID = $guildID)";
    
    $result = mysqli_query($con, $query);

    $rows = array();
    while($r = mysqli_fetch_assoc($result)) {
        $rows[] = $r;
    }

    echo json_encode($rows);

    $con->close();
}
?>
