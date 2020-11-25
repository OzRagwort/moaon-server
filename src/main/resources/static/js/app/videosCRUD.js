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
            url: '/api/moaon/v1/videos',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('등록되었습니다.');
            window.location.href = '/admin/videos/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    update : function() {
        var videoId = $('#updateVideoId').val();

        $.ajax({
            type: 'PUT',
            url: '/api/moaon/v1/videos/'+videoId
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/admin/videos/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    list : function() {
        var maxResult = $('#select-video-maxResult').val();
        var page = 1;
        var idx = $('#txt-video-idx').val();
        var vid = $('#txt-video-id').val();
        var cid = $('#txt-channel-idx').val();
        var category = $('#txt-category-idx').val();

        var url = '/admin/videos/list' +
        '?maxResult=' + maxResult +
        '&page=' + page;

        if (idx) {url = url + '&no=' + idx;}
        if (vid) {url = url + '&id=' + vid;}
        if (cid) {url = url + '&channel=' + cid;}
        if (category) {url = url + '&category=' + category;}

        window.location.href = url;
    },
    delete : function() {
        var id = $('#deleteVideoIdx').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/moaon/v1/videos/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/admin/videos/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();