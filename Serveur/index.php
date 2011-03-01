<?php
echo 'Hello '.(isset($_GET['data']) && NULL != $_GET['data'] ? $_GET['data'] : 'world').'!';
?>