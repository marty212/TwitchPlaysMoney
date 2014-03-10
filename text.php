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
INSERT INTO texts values(DEFAULT, '" . $_REQUEST['Body'] . "' )";
    $query = mysqli_query($server, $myquery);
     
    mysqli_close($server);
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<Response>
<Message>thanks for the message!</Message>
</Response>