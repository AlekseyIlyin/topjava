const mealAjaxUrl = "profile/meals/";

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
});

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTable);
}

function onFilter() {
    let filter = '';
    let fields = $('#filter input').serializeArray();
    $.each(fields, function ( index, field) {
        if (filter) {
            filter = filter.concat('&');
        }
        filter = filter.concat(field.name, '=', field.value);
    })
    if (filter.length > 0) {
        filter = 'filter?' + filter
        $.get(mealAjaxUrl.concat(filter), function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
    } else {
        updateTable();
    }
}