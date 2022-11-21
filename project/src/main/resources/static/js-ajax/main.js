$(document).ready(function () {
    loadCountRequests();
});

function loadCountRequests() {
    $.ajax({
        type: "GET",
        url: "/api/v1/connections/requests",
        success: function (data) {
            var bell = '<i class="fa fa-bell" aria-hidden="true"></i>';
            if (data != undefined && data.length > 0) {
                $('.bell').append(bell);
            }
        }
    });
}