var page = 0;
var size = 5;
var name = '';

$(document).ready(function () {
    loadUsers();

    $('#user-more').on('click', function () {
        loadUsers();
    });

    $('#search').on('click', function () {
        name = $('#search-input').val();
        page = 0;
        clearUsersList();
        loadUsers();
    });
});

function loadUsers() {
    name = $('#search-input').val();

    $.ajax({
        type: 'GET',
        url: '/users/paging?page=' + page + '&size=' + size + '&filter=' + name,
        success: function (data) {
            $('#user-content').append(data);
            page++;
        }
    });
}

function clearUsersList() {
    page = 0;
    name = '';
    $('#user-content').empty();
}

