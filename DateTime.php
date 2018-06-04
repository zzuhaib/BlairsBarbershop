<?php
    $connect = mysqli_connect("sql111.epizy.com", "epiz_22182747", "barBER319", "epiz_22182747_blairsbarbershop");
    
	$name = $_POST["name"];
    $date = $_POST["date"];
    $time = $_POST["time"];

     function scheduleAppointment() {
        global $connect, $name, $date, $time;
        $statement = mysqli_prepare($connect, "INSERT INTO user (name, date, time) VALUES (?, ?, ?)");
        mysqli_stmt_bind_param($statement, "sss", $name, $date, $time);
        mysqli_stmt_execute($statement);
        mysqli_stmt_close($statement);     
    }

    function appointmentAvailable() {
        global $connect, $date, $time;
        $statement = mysqli_prepare($connect, "SELECT * FROM user WHERE date = ? AND time = ?"); 
        mysqli_stmt_bind_param($statement, "ss", $date, $time);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }

    $response = array();
    $response["success"] = false;
  
    if (appointmentAvailable()){
        scheduleAppointment();
        $response["success"] = true;  
    }
	
	header('Content-Type: application/json');
    echo json_encode($response);
?>