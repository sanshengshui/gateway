var fragment_polish = function(id) {
    var node = $('#menu-' + id);
    node.addClass("active");
    $(".content-header > h1").html(node.text());
    if (id != 'home') {
        $(".breadcrumb").prepend('<li>' + node.text() + '</li>');
    }

    var _node = node.parent().parent();
    if (_node.is('li')) {
        _node.addClass("active");
        $(".breadcrumb").prepend('<li>' + _node.find("a").first().text() + '</li>');

        __node = _node.parent().parent();
        if (__node.is('li')) {
            __node.addClass("active");
            $(".breadcrumb").prepend('<li>' + __node.find("a").first().text() + '</li>');
        }
    }

    $(".breadcrumb").prepend('<li><a href="/"><i class="fa fa-dashboard"></i> 首页</a></li>');
}

var getUrlParam = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]); return null;
}
