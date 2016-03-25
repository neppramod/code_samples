<?php

return array(
    'assetic_configuration' => array(
        'routes' => array(
            'quarter(/default)?' => array(
                '@quarter_css',
                '@quarter_js',
            ),
        ),
        'modules' => array(
            'quarter' => array(
                'root_path' => __DIR__ . '/../assets',
                'collections' => array(
                    'quarter_css' => array(
                        'assets' => array(
                            'css/quarter.css',
                            'css/quarter-time-frame.css'
                        ),
                        'filters' => array(
                            '?CssMinFilter' => array(
                                'name' => 'Assetic\Filter\CssMinFilter'
                            ),
                        ),
                        'options' => array(
                            'output' => 'css/quarter.css'
                        )
                    ),
                    'quarter_js' => array(
                        'assets' => array(
                            'js/quarter.js',
                            'js/quarter-time-frame.js'
                        ),
                        'filters' => array(
                            '?UglifyJs2Filter' => array(
                                'name' => 'Assetic\Filter\UglifyJs2Filter'
                            )
                        ),
                        'options' => array(
                            'output' => 'js/quarter.js'
                        )
                    ),
                ),
            ),
        ),
    ),
);
