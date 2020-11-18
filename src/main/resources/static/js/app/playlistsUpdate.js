var main = {
    init : function() {
        var _this = this;
        $('#btn-playlist-update').on('click', function() {
            _this.playlist();
        });
    },
    playlist : function() {
        var data = {
            channelId : $('#updatePlaylistId').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/moaon/v1/videos/uploadslist',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(responce) {
            alert(responce.length + ' : 글이 수정되었습니다.');
            window.location.href = '/admin/videos/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();