<?php
	include 'koneksi.php';
	$kode_booking			= $_POST['kode_booking'];

	$get_kode_payment = mysqli_query($conn, "SELECT kode_payment FROM tbbooking INNER JOIN tbpayment ON tbbooking.kode_booking = tbpayment.kode_booking  WHERE tbpayment.kode_booking = '".$kode_booking."'");

	$row = mysqli_fetch_row($get_kode_payment);


	$get_kode_kamar = mysqli_query($conn, "SELECT kode_kamar FROM tbbooking WHERE kode_booking = '".$kode_booking."'");

	$row2 = mysqli_fetch_row($get_kode_kamar);
	
	$query = "DELETE FROM tbbooking WHERE (kode_booking='".$kode_booking."')";
	$query2 = "DELETE FROM tbpayment WHERE (kode_payment='".$row[0]."')";
	$query3 = "UPDATE tbkamar SET status_kamar='Tersedia' WHERE kode_kamar = '".$row2[0]."'";

	$result = mysqli_query($conn, $query);
	$result2 = mysqli_query($conn, $query2);
	$result3 = mysqli_query($conn, $query3);

			if ($result == 1 && $result2 == 1 && $result3 == 1){
				$response["message"]="Success Delete File";
			}
			else{
				$response["message"]="Fail Delete File";
			}
			echo json_encode($response);
			mysqli_close($conn);

?>