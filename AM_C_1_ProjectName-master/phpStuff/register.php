<?php
include('dbConnection.php'); 

error_reporting(-1);
ini_set('display_errors','On');

$usern = $_GET["userName"];
$pass = $_GET["password"];
register($usern, $pass);

function register($usern, $pass){
    global $con;
    $query = "Select * from user where userName = $usern";

    $result = $mysql_query($con, $query);

    if($result->num_rows != 0){
        $data = ['result' => 0];
        echo json_encode($data);
    }
    else{

        $query = "INSERT INTO user (userName, password) VALUES (" . $usern . "," . $pass . ")";

        $con->query($query);

        $success = 1;

        $data = ['result' => $success];
        echo json_encode($data);
    }

    $con->close();
}
?>