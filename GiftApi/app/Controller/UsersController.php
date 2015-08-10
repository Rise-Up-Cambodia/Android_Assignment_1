<?php
App::uses('File', 'Utility');
/*
 * Created by Sreyleak 05/08/2015
 * */
class UsersController extends AppController{
    public  $components = array('RequestHandler');
    public function index() {
        $users = $this->User->find('all');
        $this->set(array(
            'users' => $users,
            '_serialize' => array('users')
        ));
    }
    public function view($name,$password) {

      //  $users =  $this->User->findById($id);
        $users = $this->User->find('all',array(
            'conditions' => array(
                'name' => $name,
                'password' => $password
            )
        ));
        if ($users) {
            $users = $users;
        } 
        $this->set(array(
            'users' => $users,
            '_serialize' => array('users')
        ));
    }
    public function signupauthentication($email) {

        //  $users =  $this->User->findById($id);
        $users = $this->User->find('all',array(
            'conditions' => array(
                'email' => $email

            )
        ));
        if ($users) {
            $users = $users;
        }
        $this->set(array(
            'users' => $users,
            '_serialize' => array('users')
        ));
    }
    public function signup() {

        $this->User->create();
        if ($this->User->save($this->request->data)) {
            $message = 'Created';
        } else {
            $message = 'Error';
        }
        $this->set(array(
            'message' => $message,
            '_serialize' => array('message')
        ));
    }
}
?>