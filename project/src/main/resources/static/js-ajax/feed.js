var page = 0;
var size = 5;
var title = '';
var categoryId = 0;
var sort = '';
var path = '';

$(document).ready(function () {
    path = $('#path').val();
    loadPosts();

    $('#feed-more').on('click', function () {
        loadPosts();
    });

    $('#search').on('click', function () {
        clearFeed();
        title = $('#search-input').val();
        loadPosts();
    });

    $('#date').on('click', function () {
        clearFeed();
        loadPosts();
    });

    $('#likes').on('click', function () {
        clearFeed();
        sort = 'likes';
        loadPosts();
    });

    $('#comments').on('click', function () {
        clearFeed();
        sort = 'comments';
        loadPosts();
    });

    $('.cat').on('click', function () {
        clearFeed();
        categoryId = $(this).attr("id");
        loadPosts();
    });
});

function loadPosts() {
    $.ajax({
        type: 'GET',
        url: path + '/paging?page=' + page + '&size=' + size + '&title=' + title + '&category=' + categoryId + '&sort=' + sort,
        success: function (data) {
            $('#feed-content').append(data);
            page++;
        }
    });
}

function clearFeed() {
    page = 0;
    title = '';
    sort = '';
    categoryId = 0;
    $('#feed-content').empty();
}

