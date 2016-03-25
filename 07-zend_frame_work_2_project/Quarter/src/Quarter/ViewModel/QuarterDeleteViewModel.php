<?php namespace Quarter\ViewModel;

use Application\ViewModel\AbstractViewModel;
use Zend\Http\Request;
use Zend\Stdlib\RequestInterface;

class QuarterDeleteViewModel extends AbstractViewModel
{
    /**
     * Constructor
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Deletes a quarter
     * @param $id int
     * @param $request Request|RequestInterface
     * @return bool
     */
    public function delete($id, $request)
    {
        if ($request->isPost()) {
            $quarter = $this->controller->readQuarter($id);
            $this->controller->deleteQuarter($quarter);
            $this->messenger->addSuccessMessage(sprintf("%s quarter successfully deleted.", (string)$quarter));

            return true;
        }
        return false;
    }
}