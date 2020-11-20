var main = {
    init : function() {
        var _this = this;
        $('#btn-playlist-update').on('click', function() {
            _this.playlist();
        });
        $('#btn-playlist-update-multi').on('click', function() {
            _this.multiple_playlists();
        });
    },
    playlist : function() {
        this.playlistDo($('#updatePlaylistId').val());

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
    },
    multiple_playlists : function() {
        var playlistIds = $('#updateMultiPlaylistId').val();
        var playlistIdsArray = playlistIds.split(',');
        var failList = [];

        for(var i = 0 ; i < playlistIdsArray.length ; i++) {
            if ( this.playlistDo(playlistIdsArray[i]) ) {
                failList.push(playlistIdsArray[i]);
            }
        }

        alert("OK");
        $('#returnText').val(failList);

    },
    playlistDo : function(channelId) {
        var data = {
            channelId : channelId
        }

        $.ajax({
            type: 'POST',
            url: '/api/moaon/v1/videos/uploadslist',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            return false;
        }).fail(function(error) {
            return true;
        });
    }
};

main.init();