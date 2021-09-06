var main = {
    init : function() {
        var _this = this;
        $('#btn-playlist-update').on('click', function() {
            _this.playlist();
        });
    },
    playlist : function() {
        var cid = $('#updatePlaylistId').val();
        var key = $('#updatePlaylistSecretKey').val();
        var data = {
            secret : key
        };

        $.ajax({
            type: 'PUT',
            url: '/admin/channels/crud/' + cid + '/upload',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('영상 정보가 저장되었습니다.');
            window.location.href = '/admin/videos/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();