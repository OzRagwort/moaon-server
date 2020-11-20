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
        var count = 0;

        for(var i = 0 ; i < playlistIdsArray.length ; i++) {
            var j = 0;
            j = this.playlistDo(playlistIdsArray[i]);
            if ( j == 0 ) {
                failList.push(playlistIdsArray[i]);
            }
            console.log(j);
            count += j;
            console.log(count);
        }

        $('#returnText').val(failList);

    },
    playlistDo : function(channelId) {
        var count;
        var data = {
            channelId : channelId
        }

        $.ajax({
            type: 'POST',
            url: '/api/moaon/v1/videos/uploadslist',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            async: false
        }).done(function(responce) {
            count = responce.length;
        }).fail(function(error) {
            count = 0;
        });

        return count;
    }
};

main.init();