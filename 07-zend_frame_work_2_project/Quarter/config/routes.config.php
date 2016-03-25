<?php

return array(
    'router' => array(
        'routes' => array(
            'quarter' => array(
                'type' => 'Literal',
                'options' => array(
                    'route' => '/quarter',
                    'defaults' => array(
                        '__NAMESPACE__' => 'Quarter\Controller',
                        'controller' => 'Quarter',
                        'action' => 'index',
                    ),
                ),
                'may_terminate' => true,
                'child_routes' => array(
                    'default' => array(
                        'type' => 'Segment',
                        'options' => array(
                            'route' => '/[:controller[/:action[/:id]]]',
                            'constraints' => array(
                                'controller' => '[a-zA-Z][a-zA-Z0-9_-]*',
                                'action' => '[a-zA-Z][a-zA-Z0-9_-]*',
                                'id' => '[0-9]*',
                            ),
                            'defaults' => array(),
                        ),
                    ),
                ),
            ),
        ),
    ),
);