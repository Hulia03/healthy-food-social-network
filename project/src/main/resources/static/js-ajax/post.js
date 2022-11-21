var postId = 0;
var likePostHtml = ' <i id="like-post" class="fa fa-thumbs-o-up like">\n' +
    '                                  Like\n' +
    '                                 </i>';
var dislikePostHtml = ' <i id="dislike-post" class="fa fa-thumbs-o-up dislike">\n' +
    '                                  Dislike\n' +
    '                                 </i>';

$(document).ready(function () {
    postId = $("#postId").val();
    $('#like-btn').on('click', function () {
        likePost(postId);
        $('#like-btn').empty();
        $('#dislike-btn').append(dislikePostHtml);
    });

    $('#dislike-btn').on('click', function () {
        likePost(postId);
        $('#dislike-btn').empty();
        $('#like-btn').append(likePostHtml);
    });
});

function likePost(postId) {
    $.ajax({
        type: 'POST',
        url: '/api/v1/posts/' + postId + '/likes',
        success: function (data) {
            $('#post-likes-count').empty();
            $('#post-likes-count').append(data);
        }
    });
}