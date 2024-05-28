<?php
	include 'koneksi.php';
	$nama_pemesan			= $_POST['nama_pemesan'];
	$kode_booking 			= $_POST['kode_booking'];
	$kode_kamar				= $_POST['kode_kamar'];
	$date_check_in 			= $_POST['date_check_in'];
	$date_check_out			= $_POST['date_check_out'];
    $metode_payment         = $_POST['metode_payment'];
    $tipe_kamar             = $_POST['tipe_kamar'];
    $lama_inap              = $_POST['lama_inap'];


	$result = mysqli_query($conn, "UPDATE tbbooking SET status_booking = 'Selesai' WHERE kode_booking='".$kode_booking."'");
	$result2 = mysqli_query($conn, "UPDATE tbkamar SET status_kamar ='Tersedia' WHERE kode_kamar='".$kode_kamar."'");

    $response["message"]="Success";
    
    echo json_encode($response);

    mysqli_close($conn);
?>

