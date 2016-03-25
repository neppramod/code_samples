$(function () {
    QuarterTimeFrameIndex.initialize();
    QuarterTimeFrameForm.initialize();
});

var QuarterTimeFrameIndex = {
    initialize: function () {
        // Specify first sorted column's date format.
        $.fn.dataTable.moment('MM/DD/YYYY');

        var table = $('#quartertimeframe-index-table');
        if (table.size() > 0) {
            var ageGroupIndexTable = table.dataTable({
                order: [[2, 'asc']],
                columnDefs: [
                    {targets: 0, visible: false},
                    {targets: [6, 7, 8], orderable: false}
                ]
            });
        }
    }
};

var QuarterTimeFrameForm = {
    initialize: function () {

        // Start Date / End Date / Availability Deadline Mask
        $('#quartertimeframe-form-startdate').add('#quartertimeframe-form-enddate').add('#quartertimeframe-form-availabilitydeadline').inputmask({
            alias: 'mm/dd/yyyy'
        });

        // Show Availability Deadline only if Take Availability is checked
        var takeAvailabilityCheckbox = $('#quartertimeframe-form-takeavailability');
        var availabilityDeadlineInput = $('#quartertimeframe-form-availabilitydeadline');

        QuarterTimeFrameForm.toggleAvailabilityDeadlineInput(takeAvailabilityCheckbox, availabilityDeadlineInput);
        takeAvailabilityCheckbox.change(function () {
            QuarterTimeFrameForm.toggleAvailabilityDeadlineInput(takeAvailabilityCheckbox, availabilityDeadlineInput);
        });
    },
    toggleAvailabilityDeadlineInput: function (takeAvailabilityCheckbox, availabilityDeadlineInput) {
        if (takeAvailabilityCheckbox.is(':checked')) {
            availabilityDeadlineInput.parent().show().end().attr('required', 'required');
        } else {
            availabilityDeadlineInput.parent().hide().end().removeAttr('required');
        }
    }
};