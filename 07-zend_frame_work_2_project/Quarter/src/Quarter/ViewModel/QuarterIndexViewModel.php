<?php

namespace Quarter\ViewModel;

use Application\ViewModel\AbstractViewModel;

class QuarterIndexViewModel extends AbstractViewModel
{
    public function __construct()
    {
        parent::__construct();
        $this->setVariable('quarters', $this->controller->searchQuarters());
    }
}