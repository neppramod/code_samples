<?php namespace Quarter\Controller;

use Application\Controller\AbstractController;
use Quarter\ViewModel\QuarterDeleteViewModel;
use Quarter\ViewModel\QuarterFormViewModel;
use Quarter\ViewModel\QuarterIndexViewModel;

/**
 * Allows an Admin to CRUD quarters.
 *
 * @author Pramod Nepal
 * @package Quarter\Controller
 */
class QuarterController extends AbstractController
{
    public function indexAction()
    {
        return new QuarterIndexViewModel();
    }

    public function newAction()
    {
        return new QuarterFormViewModel();
    }

    public function createAction()
    {
        $model = new QuarterFormViewModel('quarter/quarter/new.phtml');

        if ($model->create($this->getRequest())) {
            return $this->redirect()->toRoute('quarter/default', array('controller' => 'quarter', 'action' => 'index'));
        }

        return $model;
    }

    public function editAction()
    {
        $model = new QuarterFormViewModel();
        $model->bind($this->params()->fromRoute('id', 0));
        return $model;
    }

    public function updateAction()
    {
        $model = new QuarterFormViewModel('quarter/quarter/edit.phtml');

        if ($model->update($this->params()->fromRoute('id', 0), $this->getRequest())) {
            return $this->redirect()->toRoute('quarter/default', array('controller' => 'quarter', 'action' => 'index'));
        }

        return $model;
    }

    public function deleteAction()
    {
        $model = new QuarterDeleteViewModel();

        $model->delete($this->params()->fromRoute('id', 0), $this->getRequest());

        return $this->redirect()->toRoute('quarter/default', array('controller' => 'quarter', 'action' => 'index'));
    }
}