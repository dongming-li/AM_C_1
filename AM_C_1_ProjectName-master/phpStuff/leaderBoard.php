<?php
error_reporting(-1);
ini_set('display_errors','On');

include('dbConnection.php');

$pos = $_GET['pos'];


getUserFromPos($pos);

function getUserFromPos($pos){
    global $con;
    $query = "SELECT userName FROM user";

    $result = mysqli_query($con, $query);

    if($result->num_rows > $pos){
        $rows = array();
		$cnt = 0;
        while($row = $result->fetch_array(MYSQL_BOTH)){
			$cnt = $cnt + 1;
			if($cnt = $pos){}
            $rows[] = $row;

        }

        $success = 0;
        //$pass = str_replace("\"", "", $pass);

        
        
        $data = $rows[0];
        $success = 1;
	
    }
    else{
        $success = 0;
    }		

    //$data = [ 'result' => $success, 'userID'=>$userID ];
	//$data = "ttseet";
    
	//echo $data;
	echo $rows[$pos][0];
	//echo $pos;
    $con->close();
}

/*
<?php
error_reporting(-1);
ini_set('display_errors','On');

include('dbConnection.php');

$con=mysqli_connect("mysql.cs.iastate.edu","dbu309amc1","XFsBvb1t", "db309amc1");
   $sql="SELECT userName FROM user";
   if (mysqli_connect_errno($con)) {
      echo "Error connecting to database";
   }
   $result = mysqli_query($con, "SELECT userName FROM user");
   $row = msqli_fetch_array($result);
   
   
   $data = ['list' => $row];
   
   //if($data){
	//   echo $data;
   //}
   
   
   
   echo json_encode($data);
   mysqli_close($con);

?>



*/
?>





