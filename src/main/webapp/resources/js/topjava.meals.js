const mealAjaxUrl = "profile/meals/";
let filterForm;

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "",
                    "orderable": false
                },
                {
                    "defaultContent": "",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );

    filterForm = $('#filter');
});

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTable);
}

function onFilter() {
    updateTable(ctx.ajaxUrl.concat("filter?", filterForm.serialize()));
}