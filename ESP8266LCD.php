<?php
date_default_timezone_set('America/Chicago');

echo format("Hello from the ", 20);
echo format("internet!", 20);
echo format("Time: " . date("h:i m.d.y"), 20);
echo format("IP: " . $_SERVER['REMOTE_ADDR'], 20);

function format($input, $amount) {
	$paddedStr = $input;
	
	while (strlen($paddedStr) < $amount) {
		$paddedStr = $paddedStr . " ";
	}
	
	return subStr($paddedStr, 0, $amount);
}
?>
