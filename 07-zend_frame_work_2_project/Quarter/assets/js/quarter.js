$(function () {
    QuarterIndex.initialize();
    QuarterForm.initialize();
});

var QuarterIndex = {
    initialize: function () {
        // Specify first sorted column's date format.
        $.fn.dataTable.moment('MM/DD/YYYY');

        var table = $('#quarter-index-table');
        if (table.size() > 0) {
            var ageGroupIndexTable = table.dataTable({
                order: [[3, 'asc']],
                columnDefs: [
                    {targets: 0, visible: false},
                    {targets: [7, 8, 9, 10], orderable: false}
                ]
            });
        }
    }
};

var QuarterForm = {
    initialize: function () {

        // Year Mask
        $('#quarter-form-year').inputmask({
            mask: '9999',
            placeholder: 'yyyy'
        });

        // Start Date / End Date / Availability Deadline Mask
        $('#quarter-form-startdate').add('#quarter-form-enddate').add('#quarter-form-availabilitydeadline').inputmask({
            alias: 'mm/dd/yyyy'
        });

        // Show Availability Deadline only if Take Availability is checked
        var takeAvailabilityCheckbox = $('#quarter-form-takeavailability');
        var availabilityDeadlineInput = $('#quarter-form-availabilitydeadline');

        QuarterForm.toggleAvailabilityDeadlineInput(takeAvailabilityCheckbox, availabilityDeadlineInput);
        takeAvailabilityCheckbox.change(function () {
            QuarterForm.toggleAvailabilityDeadlineInput(takeAvailabilityCheckbox, availabilityDeadlineInput);
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