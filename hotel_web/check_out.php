<?php

	include 'koneksi.php';
	$nama_pemesan			= $_POST['nama_pemesan'];
	$kode_booking 			= $_POST['kode_booking'];
	$kode_kamar				= $_POST['kode_kamar'];
	$date_check_in 			= $_POST['date_check_in'];
	$date_check_out			= $_POST['date_check_out'];
    $metode_payment         = $_POST['metode_payment'];

	$datetime1 = date_create($date_check_out);
	$datetime2 = date_create($date_check_in);

	$date_result_diff		= date_diff($datetime1, $datetime2); 

	$day_diff				= $date_result_diff->days;

    $query = "UPDATE tbpayment SET metode_payment='".$metode_payment."' WHERE kode_booking = '".$kode_booking."'";
    $result = mysqli_query($conn, $query) or die('Error query:  '.$query);

    $result2 = mysqli_query($conn, "SELECT tipe_kamar FROM tbpayment
                                    JOIN tbbooking ON tbpayment.kode_booking = tbbooking.kode_booking
                                    JOIN tbkamar ON tbbooking.kode_kamar = tbkamar.kode_kamar
                                    WHERE tbpayment.kode_booking = '".$kode_booking."'");

    if (mysqli_num_rows($result2) > 0) {
        $items = array();
        while($row = mysqli_fetch_object($result2)){
            array_push($items, $row);
        }
        
        $response['lama_inap'] = $day_diff;
        $response['data'] = $items;
        $response["message"]="Success";
    }else {
        $response['error_text'] = "Gagal";
        $response["message"]="Failed";
    }

    echo json_encode($response);
    mysqli_close($conn);
?>