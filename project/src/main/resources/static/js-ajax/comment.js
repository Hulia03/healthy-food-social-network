var commentId = 0;
var likeHtml = ' <i id="like-comment" class="fa fa-thumbs-o-up like">\n' +
    '                                  Like\n' +
    '                                 </button>';
var dislikeHtml = ' <i id="dislike-comment" class="fa fa-thumbs-o-up dislike">\n' +
    '                                  Dislike\n' +
    '                                 </i>';

$(document).ready(function () {

    $('.like-comment-btn').on('click', function () {
        commentId = $(this).attr("id");
        likeComment(commentId);
        $('.like-comment-btn.' + commentId).empty();
        $('.dislike-comment-btn.' + commentId).append(dislikeHtml);
    });


    $('.dislike-comment-btn').on('click', function () {
        commentId = $(this).attr("id");
        likeComment(commentId);
        $('.dislike-comment-btn.' + commentId).empty();
        $('.like-comment-btn.' + commentId).append(likeHtml);
    });
});

function likeComment(commentId) {
    $.ajax({
        type: 'POST',
        url: '/api/v1/comments/' + commentId + '/likes',
        success: function (data) {
            $('#comment-likes-count-' + commentId).empty();
            $('#comment-likes-count-' + commentId).append(data);
        }
    });
}