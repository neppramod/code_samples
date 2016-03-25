<?php

return array(
    'controllers' => array(
        'invokables' => array(
            'Quarter\Controller\Quarter' => 'Quarter\Controller\QuarterController',
            'Quarter\Controller\QuarterTimeFrame' => 'Quarter\Controller\QuarterTimeFrameController'
        ),
    ),

    'view_manager' => array(
        'template_path_stack' => array(
            'quarter' => __DIR__ . '/../view'
        ),
    ),
);
