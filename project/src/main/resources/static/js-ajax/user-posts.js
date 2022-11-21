var page = 0;
var size = 5;
var userId = 0;

$(document).ready(function () {
    userId = $("#userId").val();
    loadPosts();

    $("#user-feed-more").on("click", function () {
        loadPosts();
    });
});

function loadPosts() {
    $.ajax({
        type: 'GET',
        url: '/posts/users/' + userId + '/paging?page=' + page + '&&size=' + size,
        success: function (data) {
            $('#user-feed-content').append(data);
            page++;
        }
    });
}

