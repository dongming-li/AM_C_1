<?php                                                                                                                                                                   
error_reporting(-1);
ini_set('display_errors','On');

include('dbConnection.php');

$guildName = $_GET['guildName'];

getGuildMembers($guildName);

function getGuildMembers($guildName){
    global $con;
    $query = "SELECT guildEvent.EventID, guildEvent.EventName, guildEvent.Date
           FROM db309amc1.guildEvent
           inner join db309amc1.guild guild
                 on guild.guildID = guildEvent.guildID
           Where guild.guildName = $guildName;";

    $result = mysqli_query($con, $query);

    $rows = array();
    while($r = mysqli_fetch_assoc($result)) {
        $rows[] = $r;
    }

    echo json_encode($rows);

    $con->close();
}
?>

