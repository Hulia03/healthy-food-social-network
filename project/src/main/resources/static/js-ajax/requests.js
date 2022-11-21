var page = 0;
var size = 5;

$(document).ready(function () {
    loadRequests();

    $('#requests-more').on('click', function () {
        loadRequests();
    });
});

function loadRequests() {
    $.ajax({
        type: 'GET',
        url: '/connections/requests/paging?page=' + page + '&size=' + size,
        success: function (data) {
            $('#requests-content').append(data);
            page++;
        }
    });
}

