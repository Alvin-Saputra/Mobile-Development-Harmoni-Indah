<?php
	include 'koneksi.php';
	$kode_booking			= $_POST['kode_booking'];
	$kode_kamar				= $_POST['kode_kamar'];

	$get_kode_payment = mysqli_query($conn, "SELECT kode_payment FROM tbbooking INNER JOIN tbpayment ON tbbooking.kode_booking = tbpayment.kode_booking  WHERE tbpayment.kode_booking = '".$kode_booking."'");

	$row = mysqli_fetch_row($get_kode_payment);

	// $get_kode_Kamar = mysqli_query($conn, "SELECT kode_kamar FROM tbbooking WHERE kode_booking = '".$kode_booking."'");

	// $row = mysqli_fetch_row($get_kode_Kamar);
	
	
	$query = "DELETE FROM tbbooking WHERE (kode_booking='".$kode_booking."')";
	$query2 = "DELETE FROM tbpayment WHERE (kode_payment='".$row[0]."')";
	$query3 = "UPDATE tbkamar SET status_kamar='Tersedia' WHERE kode_kamar = '".$kode_kamar."'";

	$result3 = mysqli_query($conn, $query3);
	$result = mysqli_query($conn, $query);
	$result2 = mysqli_query($conn, $query2);

			if ($result == 1 && $result2 == 1 && $result3 == 1){
				$response["message"]="Success Delete File";
			}
			else{
				$response["message"]="Fail Delete File";
			}
			echo json_encode($response);
			mysqli_close($conn);

?>