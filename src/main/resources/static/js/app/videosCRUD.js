var main = {
    init : function() {
        var _this = this;
        $('#btn-video-save').on('click', function() {
            _this.save();
        });
        $('#btn-video-update').on('click', function() {
            _this.update();
        });
        $('#btn-video-list').on('click', function() {
            _this.list();
        });
        $('#btn-video-delete').on('click', function() {
            _this.delete();
        });
    },
    save : function() {
        var data = {
            videoId: $('#saveVideoId').val()
        };

        $.ajax({
            type: 'POST',
            url: '/admin/videos/crud',
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function() {
            alert('등록되었습니다.');
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    update : function() {
        var data = {
            videoId: $('#updateVideoId').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/admin/videos/crud'
        }).done(function() {
            alert('영상 정보가 최신화되었습니다.');
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    list : function() {
        var size = $('#select-video-size').val();
        var page = 0;
        var idx = $('#txt-video-idx').val();
        var vid = $('#txt-video-id').val();
        var cid = $('#txt-channel-idx').val();
        var category = $('#txt-category-idx').val();

        var url = '/admin/videos/list' +
        '?size=' + size +
        '&page=' + page;

        if (idx) {url = url + '&id=' + idx;}
        if (vid) {url = url + '&videoId=' + vid;}
        if (cid) {url = url + '&channelId=' + cid;}
        if (category) {url = url + '&categoryId=' + category;}

        window.location.href = url;
    },
    delete : function() {
        var id = $('#deleteVideoId').val();

        $.ajax({
            type: 'DELETE',
            url: '/admin/videos/crud/'+id,
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert('영상 정보가 삭제되었습니다.');
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();