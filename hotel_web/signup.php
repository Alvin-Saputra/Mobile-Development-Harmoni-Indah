<?php
include 'koneksi.php';

$username = $_POST['username'];
$password = $_POST['password'];

$query = "INSERT INTO tbsignup (username, password) VALUES ('".$username."', '".$password."')";
$result = mysqli_query($conn, $query);

$response = array();

if ($result == 1) {
    $response["error"] = false;
    $response["error_text"] = "Success";
} else {
    $response["error"] = true;
    $response["error_text"] = "Fail";
}

echo json_encode($response);
mysqli_close($conn);
?>
