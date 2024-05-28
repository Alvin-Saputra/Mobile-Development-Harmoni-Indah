<?php

	include 'koneksi.php';
	$nama_pemesan			= $_POST['nama_pemesan'];
	$kode_booking 			= $_POST['kode_booking'];
	$kode_kamar				= $_POST['kode_kamar'];
	$date_check_in 			= $_POST['date_check_in'];
	$date_check_out			= $_POST['date_check_out'];



	$datetime1 = date_create($date_check_out);
	$datetime2 = date_create($date_check_in);

	$date_result_diff		= date_diff($datetime1, $datetime2); 

	$day_diff				= $date_result_diff->days;


	$result = mysqli_query($conn, "SELECT harga_per_malam FROM tbkamar WHERE kode_kamar='".$kode_kamar."'");

   
     $row = mysqli_fetch_row($result);

    // echo $row[0]; // 42
    // echo $row[1]; // the email value

	$total_payment 			= $day_diff * $row[0]; 

	$get_kode_payment = mysqli_query($conn, "SELECT kode_payment FROM tbbooking INNER JOIN tbpayment ON tbbooking.kode_booking = tbpayment.kode_booking  WHERE tbpayment.kode_booking = '".$kode_booking."'");

	$row2 = mysqli_fetch_row($get_kode_payment);


	$query = "UPDATE tbbooking SET nama_pemesan='".$nama_pemesan."', waktu_check_in='".$date_check_in."', waktu_check_out= '".$date_check_out."' WHERE kode_booking='".$kode_booking."'";

	$query2 = "UPDATE tbpayment SET total_payment='".$total_payment."' WHERE kode_payment='".$row2[0]."'";
	

	$result2 = mysqli_query($conn, $query) or die('Error query:  '.$query);
	$result3 = mysqli_query($conn, $query2) or die('Error query:  '.$query2);

	$response = array();

		if ($result2 == 1 && $result3 == 1){
		$response["error"] = false;
		$response["message"]="Success Update Booking";
		}

		else{
		$response["error"] = true;
		$response["message"]="Failed To Update Booking";
		}

		echo json_encode($response);
		mysqli_close($conn);
?>