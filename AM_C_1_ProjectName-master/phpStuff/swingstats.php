<?php
include('dbConnection.php');

error_reporting(-1);
ini_set('display_errors','On');

$xmax = $_GET["XMax"];
$xmin = $_GET["XMin"];
$xavg = $_GET["XAverage"];

$ymax = $_GET["YMax"];
$ymin = $_GET["YMin"];
$yavg = $_GET["YAverage"];

$zmax = $_GET["ZMax"];
$zmin = $_GET["ZMin"];
$zavg = $_GET["ZAverage"];
insertStats($xmax, $xmin, $xavg, $ymax, $ymin, $yavg, $zmax, $zmin, $zavg);

function insertStats($xmax, $xmin, $xavg, $ymax, $ymin, $yavg, $zmax, $zmin, $zavg){
    global $con;
    $query = "INSERT INTO swingStats (XMax, XMin, XAverage, YMax, YMin, YAverage, ZMax, ZMin, ZAverage) 
VALUES (" . $xmax . "," . $xmin . "," . $xavg . "," . $ymax . "," . $ymin . "," . $yavg . "," . $zmax . "," . $zmin . "," . $zavg .")";

    $con ->query($query);
    $con ->close();
}
?>
