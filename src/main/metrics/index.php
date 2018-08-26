This page is the destination for metric uploads.

<?php
$target_dir = "run/";
$json = file_get_contents('php://input');
$file = fopen($target_dir . time(), "w");
fwrite($file, $json);
fclose($file);
?>