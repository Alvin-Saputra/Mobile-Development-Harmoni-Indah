<?php
	include 'koneksi.php';
	
	// $result = mysqli_query($conn, "SELECT * FROM tbkamar INNER JOIN tbbooking on tbkamar.kode_kamar = tbbooking.kode_kamar WHERE status_booking = 'Dalam Masa Booking'");

	$result = mysqli_query($conn, "SELECT * FROM tbkamar INNER JOIN tbbooking on tbkamar.kode_kamar = tbbooking.kode_kamar");

	if (mysqli_num_rows($result) > 0) {
		$items = array();
		while($row = mysqli_fetch_object($result)){
			array_push($items, $row);
		}
		
		$response['error_text'] = "Berhasil";
		$response['data'] = $items;
	} 
	
	else {
		$response['error_text'] = "Gagal";
	}
	 
	echo json_encode($response);
?>