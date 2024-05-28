<?php

	include 'koneksi.php';
	$kode_booking 			= $_POST['kode_booking'];

	$query = "SELECT * FROM tbpayment where kode_booking = '".$kode_booking."'";
    $result = mysqli_query($conn, $query) or die('Error query:  '.$query);

     if (mysqli_num_rows($result) > 0) {
        $items = array();
        while($row = mysqli_fetch_object($result)){
            array_push($items, $row);
        }
        $response['data'] = $items;
        $response["message"]="Success";
    }
    else {
        $response['error_text'] = "Gagal";
        $response["message"]="Failed";
    }

    echo json_encode($response);
    mysqli_close($conn);
?>