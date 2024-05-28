<?php
	include 'koneksi.php';
	$kode_kamar 			= $_POST['kode_kamar'];
	
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

?>