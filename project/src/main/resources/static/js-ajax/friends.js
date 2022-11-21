var page = 0;
var size = 5;

$(document).ready(function () {
    loadFriends();

    $('#friends-more').on('click', function () {
        loadFriends();
    });
});

function loadFriends() {
    $.ajax({
        type: 'GET',
        url: '/connections/paging?page=' + page + '&size=' + size,
        success: function (data) {
            $('#friends-content').append(data);
            page++;
        }
    });
}

