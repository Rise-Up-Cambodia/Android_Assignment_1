<?php
    App::uses('File', 'Utility');
    class GiftsController extends AppController{
       public  $components = array('RequestHandler');


 //* Vanda 05/08/2015
 // Get gift data from database //
        public function index($page) {

            $gifts = $this->Gift->find('all',
                array(
                    'fields' => array('Gift.id', 'Gift.cat_id','Gift.description','Gift.from','Gift.date','Gift.status',
                        'Gift.receive_date','Gift.gift_name','category.cat_name','user.name','user.user_profile'),
                    'joins' => array(array('table' => 'categories',
                        'alias' => 'category',
                        'type' => 'INNER',
                        'conditions' => array('category.id = Gift.cat_id'),
                    ),
                   array('table' => 'users',
                        'alias' => 'user',
                        'type' => 'INNER',
                        'conditions' => array('user.id = Gift.user_id','status'=>1),

                    )),
                    'order' => array('Gift.id DESC'),
                    'limit' => $page
                )
            );
            $this->set(array(
                'gifts' => $gifts,
                '_serialize' => array('gifts')
            ));
        }

        //* Vanda 05/08/2015
        // Get data from database //


        public function view($id) {

            $gifts =  $this->Gift->find('all',array(
                    'fields' => array('Gift.id', 'Gift.cat_id','Gift.description','Gift.from','Gift.date','Gift.status',
                        'Gift.receive_date','Gift.gift_name','category.cat_name','user.name','user.user_profile'),
                    'joins' => array(
                        array('table' => 'categories',
                        'alias' => 'category',
                        'type' => 'INNER',
                        'conditions' => array('category.id = Gift.cat_id')
                         ),
                        array('table' => 'users',
                            'alias' => 'user',
                            'type' => 'INNER',
                            'conditions' => array('user.id = Gift.user_id')

                        )
                    ),
                    'conditions' => array(
                        'Gift.id' => $id
                    )
                )
        );

            $this->set(array(
                'gifts' => $gifts,
                '_serialize' => array('gifts')
            ));
        }

    //* Vanda 05/08/2015
    // Add gift to database //

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


        //* Vanda 05/08/2015
        // Delete  gift to database by just update status to 0//

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

        //* Vanda 05/08/2015
        // Delete  gift to database by just update status to 0//

        public function delete($id) {
            $this->Gift->id = $id;
//            $image_name =  $this->Gift->find('all',array('fields' => "gift_name","conditions"=>array("id"=>$id)));
            if ($this->Gift->saveField('status', 0)) {
//                $file = new File(WWW_ROOT .'img/' .$image_name[0]['Gift']['name']);
//                if ($file->exists()) {
//                    $dir = new Folder(WWW_ROOT . 'img/delete_image', true);
//                    $file->copy($dir->path . DS . $file->name);
//                }

             //   $file->delete();
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