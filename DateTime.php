<?php
    $connect = mysqli_connect("localhost", "id5863026_barbershop", "barbershop", "id5863026_barbershop");
    
    $date = $_POST["date"];
    $time = $_POST["time"];

     function scheduleAppointment() {
        global $connect, $date, $time;
        $statement = mysqli_prepare($connect, "INSERT INTO user (date, time) VALUES (?, ?)");
        mysqli_stmt_bind_param($statement, "ss", $date, $time);
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
    
    echo json_encode($response);
?>