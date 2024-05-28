<?php
	include 'koneksi.php';

	$kode_kamar 			= $_POST['kode_kamar'];
	$harga_per_malam 		= $_POST['harga_per_malam'];
	$img 					= $_POST['image'];
	$tipe_kamar				= $_POST['tipe_kamar'];
	$action 				= $_POST['action'];
	
	switch($action){
		case "add":
		date_default_timezone_set('Asia/Jakarta');
		
		$path  = 'images/' . date("d-m-Y-his") . '-' . rand(100, 10000) . '.jpg';

		// $Random = rand(1000, 10000);

		// $path  = 'images/' . $Random . '.jpg';

		// $path2  = 'http://192.168.43.33/hotel_web/images/' . $Random . '.jpg';


		// $path  = 'images/' . 'http://hotel_web/images/192.168.1.9.jpg';
		
		$query = "INSERT INTO tbkamar (kode_kamar, tipe_kamar, harga_per_malam, foto_kamar, status_kamar) VALUES ('".$kode_kamar."', '".$tipe_kamar."', '".$harga_per_malam."', '".$path."', 'Tersedia')";

		$result = mysqli_query($conn, $query) or die('Error query:  '.$query);

		$response = array();

		if ($result == 1){
		file_put_contents($path, base64_decode($img));
		$response["error"] = false;
		$response["message"]="Success Insert";
		}

		else{
		$response["error"] = true;
		$response["message"]="Failed To Insert";
		}
		echo json_encode($response);
		mysqli_close($conn);
		break;




		case "update":
		date_default_timezone_set('Asia/Jakarta');

		$path  = 'images/' . date("d-m-Y-his") . '-' . rand(100, 10000) . '.jpg';

		// $Random = rand(1000, 10000);
		
		// $path  = 'images/' . $Random . '.jpg';

		// $path2  = 'http://192.168.43.33/hotel_web/images/' . $Random . '.jpg';

		// $path2  = 'http://192.168.1.9/hotel_web/images/' . date("d-m-Y-his") . '-' . rand(100, 10000) . '.jpg';

		if($img == ""){
		$query = "UPDATE tbkamar SET tipe_kamar='".$tipe_kamar."',harga_per_malam='".$harga_per_malam."' WHERE kode_kamar='".$kode_kamar."'";

		$result = mysqli_query($conn, $query) or die('Error query:  '.$query);
		
		}

		else{
			$query = "UPDATE tbkamar SET tipe_kamar='".$tipe_kamar."', harga_per_malam='".$harga_per_malam."', foto_kamar='".$path."' WHERE kode_kamar='".$kode_kamar."'";

			$result = mysqli_query($conn, $query) or die('Error query:  '.$query);
		}

		

		$response = array();

		if ($result == 1){
			file_put_contents($path, base64_decode($img));
			$response["error"] = false;
			$response['message']="Success Update";
		}
		else{
			$response["error"] = true;
			$response['message']="Failed Update";
		}
		echo json_encode($response);
		mysqli_close($conn);
		break;





		case "delete":
			$query = "DELETE FROM tbkamar WHERE (kode_kamar='".$kode_kamar."')";
			$result = mysqli_query($conn, $query);
			if ($result == 1){
				$response["message"]="Success Delete File";
			}
			else{
				$response["message"]="Fail Delete File";
			}
			echo json_encode($response);
			mysqli_close($conn);
			break;

	}
			
?>