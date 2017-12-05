/**
 * @author Created by Administrator on 2017/6/26.
 * @description 消息格式 {"code":200,"errorMsg":"",
* "result":[{"msgType":2,"from":51,"to":56,"ts":1498529455562,"msgContent":"你有新的订单，快去处理..."}]}
 */

$.app = {
    initIndex: function () {
        var pollingUrl = "/async/comet.json";
        // var pollingUrl = "/async/polling.json";
        var longPolling = function (url, callback) {
            $.ajax({
                url: pollingUrl,
                async: true,
                cache: false,
                global: false,
                timeout: 30 * 1000,
                dataType: 'json',
                success: function (data, status, request) {
                    callback(data);
                    data = null, status = null, request = null;
                    setTimeout(function () {
                        longPolling(url, callback)
                    }, 10)
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jqXHR = null;
                    textStatus = null;
                    errorThrown = null;
                    setTimeout(function () {
                        longPolling(url, callback)
                    }, 30 * 1000)
                }
            });
        }
    }
};

$.navigation = {
    selectNavigationBar: function (id) {
        $("#" + id).click();
    },
    leftSideBar: function (name) {
        $(function () {
            $(".side .list-unstyled li").each(function () {
                if ($(this).find("c").text() == name) {
                    $(this).addClass("active");
                    return;
                }
            });
        });
    },
    leftSide: function () {
        return {
            rander: function (options) {
                var defaults = {
                    parent: $.constant.topBar,
                    index: 0,
                    child: [],
                    href: null
                };
                if (!options) {
                    options = {};
                }
                options = $.extend({}, defaults, options);
                $.navigation.selectNavigationBar(options.parent);//选中顶部菜单
                var arr = new Array();//侧边栏
                var childs = {};
                $(".side .list-unstyled li:not(:first) a c").each(function (i, e) {
                    arr.push($(e).text());
                });
                $(".side .list-unstyled li:not(:first) a").each(function (i, e) {
                    var key = $(e).attr("href");
                    childs[key] = arr[i];
                })
                options.child = childs;
                var key = window.location.pathname;
                $.navigation.leftSideBar(options.child[key]);//选中侧边栏
            }
        }
    },
    initNav: function (options) {
        if (!options) {
            options = {};
        }
        $.extend($.constant, options)
        $.navigation.leftSide().rander()
    }
};
$.constant = {
    topBar: "base",
    leftSide: "首页"
};

$.tabs = {
    initTab: function () {
        $(".nav-tab").on('click', function () {
            var id = $(this).attr("id");
            var li = $(this).parent();
            var html = li.siblings('div').html();
            $(".navbar-nav li").removeClass("active");
            li.addClass("active");
            $(".list-unstyled").children().remove();
            $(".list-unstyled").append(html);
            $.constant.topBar = _CommnUtil.notNull(id) ? id : $.constant.topBar;
            // $(".list-unstyled li:eq(1) c").click();
        });
        $("#" + $.constant.topBar).click().parent("li").addClass("active");//默认选中的nav-bar
    }
};
$.validateForm = {}
$.fn.formValidate = function (opts) {
    var rules = $.extend({}, opts ? (opts.rules || {}) : {});
    var messages = $.extend({}, opts ? (opts.messages || {}) : {});
    var defaults = $.extend(opts || {}, {
        rules: rules,
        messages: messages,
        errorElement: opts ? (opts.errorElement || "span") : "span",
        errorClass: opts ? (opts.errorClass || "errorContainer") : "errorContainer"
    });
    //$.extend($.fn.validate.prototype,__defaultOpts);
    $.fn.validate.call(this, defaults);
};


