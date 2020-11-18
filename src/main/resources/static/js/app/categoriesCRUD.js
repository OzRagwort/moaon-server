var main = {
    init : function() {
        var _this = this;
        $('#btn-category-save').on('click', function() {
            _this.save();
        });
        $('#btn-category-update').on('click', function() {
            _this.update();
        });
        $('#btn-category-list').on('click', function() {
            _this.list();
        });
        $('#btn-category-delete').on('click', function() {
            _this.delete();
        });
    },
    save : function() {
        var data = {
            categoryName: $('#saveCategoryName').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/moaon/v1/categories',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('등록되었습니다.');
            window.location.href = '/admin/categories/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    update : function() {
        var data = {
            categoryName: $('#updateCategoryName').val()
        };

        var id = $('#updateCategoryIdx').val();

        $.ajax({
            type: 'PUT',
            url: '/api/moaon/v1/categories/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/admin/categories/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },
    list : function() {
        var maxResult = $('#selete-category-maxResult').val();
        var page = 1;
        var idx = $('#txt-category-idx').val();

        var url = '/admin/categories/list' +
        '?maxResult=' + maxResult +
        '&page=' + page +
        '&no=' + idx;

        window.location.href = url;
    },
    delete : function() {
        var id = $('#deleteCategoryIdx').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/moaon/v1/categories/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/admin/categories/crud';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();