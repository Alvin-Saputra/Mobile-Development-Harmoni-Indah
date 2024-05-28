<?php

	include 'koneksi.php';
	$nama_pemesan			= $_POST['nama_pemesan'];
	$kode_booking 			= $_POST['kode_booking'];
	$kode_kamar				= $_POST['kode_kamar'];
	$date_check_in 			= $_POST['date_check_in'];
	$date_check_out			= $_POST['date_check_out'];


	date_default_timezone_set('Asia/Jakarta');

	$kode_payment			= 'pay_'.date("d-m-Y-his").rand(100, 10000);

	$datetime1 = date_create($date_check_out);
	$datetime2 = date_create($date_check_in);

	$date_result_diff		= date_diff($datetime1, $datetime2); 

	$day_diff				= $date_result_diff->days;


	$result = mysqli_query($conn, "SELECT harga_per_malam FROM tbkamar WHERE kode_kamar='".$kode_kamar."'");

   
     $row = mysqli_fetch_row($result);

    // echo $row[0]; // 42
    // echo $row[1]; // the email value

	$total_payment 			= $day_diff * $row[0]; 

	$query = "INSERT INTO tbbooking (kode_booking, kode_kamar, nama_pemesan, waktu_check_in, waktu_check_out, status_booking) VALUES ('".$kode_booking."', '".$kode_kamar."', '".$nama_pemesan."', '".$date_check_in."', '".$date_check_out."' , 'Dalam Masa Booking')";

		$query2 = "UPDATE tbkamar SET status_kamar='Tidak Tersedia' WHERE kode_kamar = '".$kode_kamar."'";

		$query3 = "INSERT INTO tbpayment (kode_payment, metode_payment, total_payment, kode_booking) VALUES ('".$kode_payment."', '', '".$total_payment."', '".$kode_booking."')";

		$result = mysqli_query($conn, $query) or die('Error query:  '.$query);
		$result2 = mysqli_query($conn, $query2) or die('Error query:  '.$query2);
		$result3 = mysqli_query($conn, $query3) or die('Error query:  '.$query3);

		$response = array();

		if ($result == 1 and $result2 == 1 and $result3 == 1){
		// file_put_contents($path, base64_decode($img));
		$response["error"] = false;
		$response["message"]="Success Insert Booking";
		}

		else{
		$response["error"] = true;
		$response["message"]="Failed To Insert Bookung";
		}
		echo json_encode($response);
		mysqli_close($conn);

?>