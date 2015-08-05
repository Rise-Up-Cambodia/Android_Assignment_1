<?php
    Router::mapResources(array('gifts','users'));
    Router::resourceMap(array(
        array('action' => 'index', 'method' => 'GET', 'id' => false),
        array('action' => 'view', 'method' => 'GET', 'id' => true),
        array('action' => 'add', 'method' => 'POST', 'id' => false),
        array('action' => 'update', 'method' => 'PUT', 'id' => true),
        array('action' => 'signup', 'method' => 'POST', 'id' => false),
        array('action' => 'delete', 'method' => 'DELETE', 'id' => true)
    ));
    Router::parseExtensions();
	require CAKE . 'Config' . DS . 'routes.php';
