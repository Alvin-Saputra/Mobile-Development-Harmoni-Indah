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


	$result = mysqli_query($conn, "SELECT harga_per_malam FROM tbkamar WHERE kode_kamar='".$kode_kamar."'");
    $row = mysqli_fetch_row($result);
	$total_payment 			= $lama_inap * $row[0]; 


        
        $response['total_payment'] = $total_payment;
        $response['harga_per_malam'] = $row[0];
        $response["message"]="Success";



    echo json_encode($response);
    mysqli_close($conn);
?>

