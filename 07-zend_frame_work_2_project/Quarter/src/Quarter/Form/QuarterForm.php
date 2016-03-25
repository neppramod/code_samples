<?php namespace Quarter\Form;

use Application\Model\Quarter;
use Zend\Form\Element;
use Zend\Form\Element\DateTime;
use Zend\Form\Element\Select;
use Zend\Form\Element\Checkbox;
use Zend\Form\Form;
use Zend\InputFilter\InputFilter;
use Zend\InputFilter\InputFilterAwareInterface;
use Zend\Validator\Callback;
use Zend\Validator\InArray;

class QuarterForm extends Form implements InputFilterAwareInterface
{
    // <editor-fold desc="Constants">

    const ID = "id";
    const YEAR = "year";
    const SESSION = "session";
    const START_DATE = "startDate";
    const END_DATE = "endDate";
    const TAKE_AVAILABILITY = "takeAvailability";
    const AVAILABILITY_DEADLINE = "availabilityDeadline";

    const TAKE_AVAILABILITY_CHECKED = 1;

    // </editor-fold>

    // <editor-fold desc="Accessors">

    /**
     * @return int|null
     */
    public function getID()
    {
        return isset($this->data[QuarterForm::ID]) ? $this->data[QuarterForm::ID] : null;
    }

    /**
     * @return string|null
     */
    public function getYear()
    {
        return isset($this->data[QuarterForm::YEAR]) ? $this->data[QuarterForm::YEAR] : null;
    }

    /**
     * @return string|null
     */
    public function getSession()
    {
        return isset($this->data[QuarterForm::SESSION]) ? $this->data[QuarterForm::SESSION] : null;
    }

    /**
     * @return string|null
     */
    public function getStartDate()
    {
        return isset($this->data[QuarterForm::START_DATE]) ? $this->data[QuarterForm::START_DATE] : null;
    }

    /**
     * @return string|null
     */
    public function getEndDate()
    {
        return isset($this->data[QuarterForm::END_DATE]) ? $this->data[QuarterForm::END_DATE] : null;
    }

    /**
     * @return string|null
     */
    public function getTakeAvailability()
    {
        return isset($this->data[QuarterForm::TAKE_AVAILABILITY]) ? $this->data[QuarterForm::TAKE_AVAILABILITY] : null;
    }

    /**
     * @return string|null
     */
    public function getAvailabilityDeadline()
    {
        // Note: ViewModels are responsible for deciding the deadline if takeAvailability is false.
        return isset($this->data[QuarterForm::AVAILABILITY_DEADLINE]) ? $this->data[QuarterForm::AVAILABILITY_DEADLINE] : null;
    }

    // </editor-fold>

    public function __construct()
    {
        parent::__construct();

        $this->createFormElements();
        $inputFilter = $this->createInputFilter();
        $this->setInputFilter($inputFilter);
    }

    //<editor-fold desc="Create Form Elements">

    private function createFormElements()
    {
        $session = new Select(QuarterForm::SESSION);
        $session->setLabel('Session');
        $session->setLabelAttributes(array('class' => 'required'));
        $session->setAttributes(array('id' => 'quarter-form-session'));
        $session->setEmptyOption('Select a session...');
        $session->setValueOptions(array_combine(Quarter::SESSIONS, Quarter::SESSIONS));
        $this->add($session);

        $year = new DateTime(QuarterForm::YEAR);
        $year->setLabel('Year');
        $year->setLabelAttributes(array('class' => 'required'));
        $year->setAttributes(array('id' => 'quarter-form-year', 'required' => 'required'));
        $year->setFormat('Y');
        $this->add($year);

        $startDate = new DateTime(QuarterForm::START_DATE);
        $startDate->setLabel('Start Date');
        $startDate->setLabelAttributes(array('class' => 'required'));
        $startDate->setAttributes(array('id' => 'quarter-form-startdate', 'required' => 'required'));
        $startDate->setFormat('m/d/Y');
        $this->add($startDate);

        $endDate = new DateTime(QuarterForm::END_DATE);
        $endDate->setLabel('End Date');
        $endDate->setLabelAttributes(array('class' => 'required'));
        $endDate->setAttributes(array('id' => 'quarter-form-enddate', 'required' => 'required'));
        $endDate->setFormat('m/d/Y');
        $this->add($endDate);

        $takeAvailability = new Checkbox(QuarterForm::TAKE_AVAILABILITY);
        $takeAvailability->setLabel('Take Availability');
        $takeAvailability->setAttributes(array('id' => 'quarter-form-takeavailability'));
        $takeAvailability->setCheckedValue(QuarterForm::TAKE_AVAILABILITY_CHECKED);
        $this->add($takeAvailability);

        $availabilityDeadline = new DateTime(QuarterForm::AVAILABILITY_DEADLINE);
        $availabilityDeadline->setLabel('Availability Deadline');
        $availabilityDeadline->setLabelAttributes(array('class' => 'required'));
        $availabilityDeadline->setAttributes(array('id' => 'quarter-form-availabilitydeadline'));
        $availabilityDeadline->setFormat('m/d/Y');
        $this->add($availabilityDeadline);

        $submit = new Element('submit');
        $submit->setAttributes(array('type' => 'Submit', 'value' => 'Submit', 'class' => 'btn btn-primary form-button'));
        $this->add($submit);
    }

    //</editor-fold>

    //<editor-fold desc="Create Input Filter">

    /**
     * @return InputFilter
     */
    private function createInputFilter()
    {
        $inputFilter = new InputFilter();

        $inputFilter->add(array(
            'name' => QuarterForm::ID,
            'required' => false
        ));

        $inputFilter->add(array(
            'name' => QuarterForm::YEAR,
            'required' => true
        ));

        $inputFilter->add(array(
            'name' => QuarterForm::SESSION,
            'required' => true,
            'validators' => array(
                array(
                    'name' => 'InArray',
                    'options' => array(
                        'haystack' => Quarter::SESSIONS,
                        'messages' => array(
                            InArray::NOT_IN_ARRAY => 'Session must be valid',
                        ),
                    ),
                ),
            )
        ));

        $inputFilter->add(array(
            'name' => QuarterForm::START_DATE,
            'required' => true
        ));

        $inputFilter->add(array(
            'name' => QuarterForm::END_DATE,
            'required' => true,
            'validators' => array(
                array(
                    'name' => 'Callback',
                    'options' => array(
                        'messages' => array(
                            Callback::INVALID_VALUE => 'End date must occur after start date',
                        ),
                        'callback' => function ($value, $context = array()) {
                            $endDate = strtotime($value);
                            $startDate = strtotime($context[QuarterForm::START_DATE]);

                            return ($endDate >= $startDate);
                        },
                    ),
                ),
            )
        ));

        $inputFilter->add(array(
            'name' => QuarterForm::TAKE_AVAILABILITY,
            'required' => true
        ));

        $inputFilter->add(array(
            'name' => QuarterForm::AVAILABILITY_DEADLINE,
            'required' => false
        ));

        return $inputFilter;
    }

    //</editor-fold>
}