var main = {
    init : function() {
        var _this = this;
        $('#btn-category-list').on('click', function() {
            _this.list();
        });
    },
    list : function() {
        var size = $('#select-category-size').val();
        var page = 0;
        var idx = $('#txt-category-idx').val();

        var url = '/admin/categories/list' +
        '?size=' + maxResult +
        '&page=' + page +
        '&id=' + idx;

        window.location.href = url;
    }
};

main.init();