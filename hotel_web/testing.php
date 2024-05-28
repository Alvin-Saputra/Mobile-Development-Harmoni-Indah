<?php
    include 'koneksi.php';
    
  $result = mysqli_query($conn, "SELECT harga_per_malam FROM tbkamar WHERE kode_kamar='R113'");

   
     $row = mysqli_fetch_row($result);

    echo $row[0]; // 42
    echo $row[1]; // the email value
?>