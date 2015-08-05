<?php
App::uses('File', 'Utility');
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
        } else {
            $users = 'Incorrect username or password !';
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