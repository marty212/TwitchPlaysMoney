<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);
    $username = ""; 
    $password = "";   
    $host = "localhost";
    $database="test";
    
    $server = mysqli_connect($host, $username, $password, $database);

	if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
  
    $myquery = "
SELECT  `time`, `str` FROM  `n0`
";
    $query = mysqli_query($server, $myquery);
    
    while($row = mysqli_fetch_array($query) ) {
		echo $row['time'];
		echo ' : ';
		echo $row['str'];
		echo '<br>';
    }   
     
    mysqli_close($server);
?>