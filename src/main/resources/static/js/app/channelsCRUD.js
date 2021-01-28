var main = {
    init : function() {
        var _this = this;
        $('#btn-channel-save').on('click', function() {
            _this.save();
        });
        $('#btn-channel-update').on('click', function() {
            _this.update();
        });
        $('#btn-channel-list').on('click', function() {
            _this.list();
        });
        $('#btn-channel-delete').on('click', function() {
            _this.delete();
        });
    },
    save : function() {
        var data = {
            categoryId: $('#saveChannelCategory').val(),
            channelId: $('#saveChannelId').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/moaon/v1/yt-channels',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            alert(response + ' 등록되었습니다.');
            window.location.href = '/admin/channels/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    update : function() {
        var data = {
            categoryId: $('#updateChannelCategory').val(),
            channelId: $('#updateChannelId').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/moaon/v1/yt-channels',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('채널 정보를 최신화하였습니다.');
            window.location.href = '/admin/channels/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    list : function() {
        var maxResults = $('#select-channel-maxResults').val();
        var page = 1;
        var idx = $('#txt-channel-idx').val();
        var cid = $('#txt-channel-id').val();
        var category = $('#txt-category-idx').val();

        var url = '/admin/channels/list' +
        '?maxResults=' + maxResults +
        '&page=' + page;

        if (idx) {url = url + '&no=' + idx;}
        if (cid) {url = url + '&id=' + cid;}
        if (category) {url = url + '&category=' + category;}

        window.location.href = url;
    },
    delete : function() {
        var id = $('#deleteChannelIdx').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/moaon/v1/channels/'+id,
            contentType: 'application/json; charset=utf-8'
        }).done(function(response) {
            alert(response + ' 채널 정보가 삭제되었습니다.');
            window.location.href = '/admin/channels/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();