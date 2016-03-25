<?php namespace Quarter\ViewModel;


use Application\Model\Quarter;
use Application\ViewModel\AbstractViewModel;
use Quarter\Form\QuarterForm;
use Zend\Http\Request;
use Zend\Stdlib\RequestInterface;

class QuarterFormViewModel extends AbstractViewModel
{
    /** @var QuarterForm */
    private $form;

    /**
     * Constructor
     * @param string $template
     */
    public function __construct($template = null)
    {
        parent::__construct(null, $template);
        $this->form = new QuarterForm();
        $this->setVariable('form', $this->form);
    }

    /**
     * Pre-populates the form with data from an existing quarter
     *
     * @param $id
     */
    public function bind($id)
    {
        $this->setVariable('id', $id);
        $this->form->bind($this->controller->readQuarter($id));
    }

    /**
     * Creates a quarter
     *
     * @param $request Request|RequestInterface
     * @return bool
     */
    public function create($request)
    {
        if ($this->isValid($request)) {

            $quarter = $this->controller->createQuarter(
                $this->form->getYear(),
                $this->form->getSession(),
                date_create($this->form->getStartDate()),
                date_create($this->form->getEndDate()),
                $this->form->getTakeAvailability(),
                $this->form->getTakeAvailability()
                    ? date_create($this->form->getAvailabilityDeadline())
                    : date_create(Quarter::AVAILABILITY_DEADLINE_PLACEHOLDER)
            );

            $this->messenger->addSuccessMessage(sprintf("%s quarter successfully created.", (string)$quarter));

            return true;
        }
        return false;
    }

    /**
     * Updates a quarter
     *
     * @param $id int
     * @param $request Request|RequestInterface
     * @return bool
     */
    public function update($id, $request)
    {
        $this->setVariable('id', $id);

        if ($this->isValid($request, $id)) {
            $quarter = $this->controller->readQuarter($id);

            $quarter->year = $this->form->getYear();
            $quarter->session = $this->form->getSession();
            $quarter->startDate = date_create($this->form->getStartDate());
            $quarter->endDate = date_create($this->form->getEndDate());
            $quarter->takeAvailability = $this->form->getTakeAvailability();
            $quarter->availabilityDeadline = $this->form->getTakeAvailability()
                ? date_create($this->form->getAvailabilityDeadline())
                : date_create(Quarter::AVAILABILITY_DEADLINE_PLACEHOLDER);
            $quarter->save();

            $this->messenger->addSuccessMessage(sprintf("%s quarter successfully updated.", (string)$quarter));

            return true;
        }
        return false;
    }

    /**
     * Sets form data and validates the form
     *
     * @param $request Request|RequestInterface
     * @param $id int ID of the existing quarter, if available
     * @return bool
     */
    private function isValid($request, $id = 0)
    {
        if ($request->isPost()) {
            $this->form->setData($request->getPost());

            $quarters = $this->controller->searchQuarters();

            return $this->form->isValid()
            && !$this->hasDuplicateSessionAndYear($quarters, $id)
            && !$this->isOverlappingWithExistingQuarter($quarters, $id);
        } else {
            return false;
        }
    }

    /**
     * Checks new quarter data to see if session/year already exist.
     *
     * @param $quarters array Array of existing quarters.
     * @param $id int ID of a quarter to omit in this check, which is necessary if an existing record is being updated.
     * @return bool
     */
    private function hasDuplicateSessionAndYear($quarters, $id = 0)
    {
        /**
         * @var $quarter \Application\Model\Quarter
         */
        foreach ($quarters as $quarter) {
            if ($quarter->getId() == $id) continue;

            if ($this->form->getSession() == $quarter->session && $this->form->getYear() == $quarter->year) {
                $this->messenger->addErrorMessage(sprintf('A %s quarter already exists.', $quarter));
                return true;
            }
        }

        return false;
    }

    /**
     * Checks new quarter data to see if start/end dates overlap with existing quarters.
     *
     * @param $quarters array Array of existing quarters.
     * @param $id int ID of a quarter to omit in this check, which is necessary if an existing record is being updated.
     * @return bool
     */
    private function isOverlappingWithExistingQuarter($quarters, $id = 0)
    {
        /**
         * @var $quarter \Application\Model\Quarter
         */
        foreach ($quarters as $quarter) {
            if ($quarter->getId() == $id) continue;

            if (date_create($this->form->getEndDate()) >= $quarter->startDate
                && date_create($this->form->getStartDate()) <= $quarter->endDate
            ) {
                $this->messenger->addErrorMessage(
                    sprintf('The Start / End Dates you specified overlap with the %s (%s - %s) quarter.',
                        $quarter,
                        $quarter->startDate->format('m/d/Y'),
                        $quarter->endDate->format('m/d/Y')
                    )
                );
                return true;
            }
        }

        return false;
    }
}