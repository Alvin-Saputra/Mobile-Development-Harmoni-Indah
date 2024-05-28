<?php
	include 'koneksi.php';

	
	$nama 			= $_POST['nama'];
	$password 		= $_POST['password'];


	$result = mysqli_query($conn, "SELECT * FROM tbsignup WHERE username = '$nama' AND password = '$password'");

	if (mysqli_num_rows($result) > 0) {
		$response['error_text'] = "Success Login";
	}
	else{
		$response['error_text'] = "Login Failed";
	}

	echo json_encode($response);
	
?>