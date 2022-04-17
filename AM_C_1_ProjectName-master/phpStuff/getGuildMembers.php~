<?php
    error_reporting(-1);
ini_set('display_errors','On');

include('dbConnection.php');

$guildName = $_GET['guildName'];

getGuildMembers($guildName);

function getGuildMembers($guildName){
    global $con;
    $query = "Select userName, password, dude.userID From db309amc1.user dude
              inner join db309amc1.guildMember member
                 on dude.userId = member.userID
              inner join db309amc1.guild guild
                 on guild.guildID = member.guildID
              Where guildName = $guildName;";
    
    $result = mysqli_query($con, $query);

    $rows = array();
    while($r = mysqli_fetch_assoc($result)) {
        $rows[] = $r;
    }

    echo json_encode($rows);

    $con->close();
}
?>

