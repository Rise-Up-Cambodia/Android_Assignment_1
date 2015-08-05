<?php
    App::uses('File', 'Utility');
    class GiftsController extends AppController{
       public  $components = array('RequestHandler');

        public function index() {
//            $gifts = $this->Gift->find('all', array(
//                'conditions' => array(
//                    'status' => 1
//                )
//            ));

            $gifts = $this->Gift->find('all',
                array(
                    'fields' => array('Gift.id', 'Gift.cat_id','Gift.description','Gift.from','Gift.date','Gift.status','recieve_date','name','category.name'),
                    'joins' => array(array('table' => 'categories',
                        'alias' => 'category',
                        'type' => 'INNER',
                        'conditions' => array('category.id = Gift.cat_id','status'=>1),

                    )),
                     'order' => array(
                'Gift.id DESC')
                )
            );
            $this->set(array(
                'gifts' => $gifts,
                '_serialize' => array('gifts')
            ));
        }
        public function view($id) {

            $gifts =  $this->Gift->findById($id);
            $this->set(array(
                'gifts' => $gifts,
                '_serialize' => array('gifts')
            ));
        }
    public function add() {

        $this->Gift->create();
                if ($this->Gift->save($this->request->data)) {
                    $message = 'Created';
                } else {
                    $message = 'Error';
                }
                $this->set(array(
                    'message' => $message,
                    '_serialize' => array('message')
                ));
        }
        public function edit($id) {
            $this->Gift->id = $id;
            if ($this->Gift->save($this->request->data)) {
                $message = 'Saved';
            } else {
                $message = 'Error';
            }
            $this->set(array(
                'message' => $message,
                '_serialize' => array('message')
            ));
        }
        public function delete($id) {
            $this->Gift->id = $id;
            $image_name =  $this->Gift->find('all',array('fields' => "name","conditions"=>array("id"=>$id)));
            if ($this->Gift->saveField('status', 0)) {
                $file = new File(WWW_ROOT .'img/' .$image_name[0]['Gift']['name']);
                if ($file->exists()) {
                    $dir = new Folder(WWW_ROOT . 'img/delete_image', true);
                    $file->copy($dir->path . DS . $file->name);
                }
               // $file = new File(WWW_ROOT . 'img/'.$image_name[0]['Gift']['image_path'], false, 0777);
                $file->delete();
                $message =   "Deleted";
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