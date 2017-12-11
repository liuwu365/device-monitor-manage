/**
 * Created by rocking on 2017/6/20 0026.
 * 后台公用js
 */
//显示左侧菜单
function showMenu(obj) {
    var html = $(obj).parent().siblings('div').html();
    $(".navbar .container-fluid ul li").removeClass("active");
    $("#" + obj.id).parent("li").addClass("active");
    $(".list-unstyled").children().remove();
    $(".list-unstyled").append(html);
}
//左侧活动选项栏目
function showSide(obj) {
    $(function () {
        $(".side .list-unstyled li").each(function () {
            if ($(this).find("c").text() == obj) {
                $(this).addClass("active");
                return;
            }
        });
    });
}

/**
 * 1.列表中的多选复选框,可实现多个勾选
 * 2.行表表格隔行换色
 */
$(document).ready(function () {
    $('body').attr('id', 'yt');
    $('[data-toggle="tooltip"]').tooltip();
    //点击上传图片按钮
    $('.up_img_btn').click(function () {
        $(this).parent().find('[type="file"]').click();
    });
    $('.up_img [type="file"]').change(function () {
        alert($(this).val());
    });

    //导入layui框架
    layui.use(['layer', 'laydate', 'form', 'upload'], function () {
        var layer = layui.layer
            , laydate = layui.laydate
            , upload = layui.upload
            , form = layui.form;
    });
    //导航的hover效果、二级菜单等功能，需要依赖element模块(修改密码二级菜单)
    layui.use('element', function () {
        var element = layui.element;
        //监听导航点击
        element.on('nav(demo)', function (elem) {
            layer.msg(elem.text());
        });
    });
    //修改密码
    $("#pwForm").validate({
        debug: true,
        checkStart: true,
        errorElement: 'span',
        errorClass: '_error',
        focusInvalid: false,
        ignore: "",
        rules: {
            "password": {required: true},
            "nPassword": {required: true},
            "nPassword2": {required: true, equalTo: "#nPassword"}
        },
        messages: {
            "password": {required: "请输入原密码"},
            "nPassword": {required: "请输入新密码"},
            "nPassword2": {required: "请输入确认密码", equalTo: "密码不一致"}
        },
        submitHandler: function (form) {
            $(form).ajaxSubmit(function (res) {
                if (res.code == 200) {
                    layer.msg(res.msg, {time: 3000}, function () {
                        window.location.reload();
                    });
                } else {
                    layer.msg(res.msg, {time: 2000});
                }
            });
        },
        highlight: function (e) {
            $(e).closest('.control-group').removeClass('info').addClass('error');
        }
    });

    //全选
    $("#select_all").click(function () {
        $("input:checkbox").prop("checked", $(this).prop("checked"));
    });
    //关联全选
    $("input:checkbox").not("#select_all").click(function () {
        if ($("input:checkbox:not(:checked)").not("#select_all").length == 0) $("#select_all").prop("checked", true);
        else $("#select_all").prop("checked", false);
    });

    //分页插件
    var options = {
        bootstrapMajorVersion: 3, //版本
        currentPage: $("[name='page']").val(),
        totalPages: $("[name='totalPage']").val(),
        alignment: "left",
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "上一页";
                case "next":
                    return "下一页";
                case "last":
                    return "末页";
                case "page":
                    return page;
            }
        },
        pageUrl: function (type, page, current) {
            if (page == current) {
                return "javascript:void(0)";
            } else {
                return "javascript:paging('" + page + "');";
            }
        }
    };
    $('#pageLimit').bootstrapPaginator(options);

    //悬浮弹框提示
    $(".alertTip").popover({
        trigger: 'hover',
        placement: 'right',
        title: '<div style="text-align:left; color:gray; font-size:12px;">提示</div>',
        html: 'true',
        animation: false
    });


});

//分页搜索查询
function paging(page) {
    $("#page").val(page);
    $("#searchForm").submit();
}
//解决筛选条件时,分页从第二页起搜索无内容的错误bug;
function resetPage() {
    $("#page").val(1);
}
//取消按钮到上个页面
function cancelGoBack() {
    window.history.back();
}
//重置按钮
$(function () {
    $("#button").click(function () {
        $("#searchForm :input").not(":button, :submit, :reset, :hidden").val("").removeAttr("checked").remove("selected");//核心
    });
})
//加载中
function loading() {
    layer.load(1, {
        shade: [0.1, '#fff'],
        content: '加载中。。。', success: function (layero) {
            layero.find('.layui-layer-content').addClass('loading');
        }
    });
}


/**
 * 项目公用
 */
var _CommonJS = {
    /**
     * 特殊字符过滤[把特殊字符踢掉]
     * @param {Object} s
     */
    checkCharFilter: function (s) {
        //var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？+%]")
        var pattern = new RegExp("[`~!@#$^*()=|{}:;,\\[\\]<>/?~！@#￥……*（）——|{}【】‘；：”“。，、？+%]");
        var rs = "";
        for (var i = 0; i < s.length; i++) {
            rs = rs + s.substr(i, 1).replace(pattern, '');
        }
        return rs;
    },
    /**
     * 把英文单词的首字母大写
     */
    replaceFirstUper: function (str) {
        if (str.length <= 0 || str == "") {
            return;
        }
        str = str.toLowerCase();
        return str.replace(/\b(\w)|\s(\w)/g, function (m) {
            return m.toUpperCase();
        });
    },
    /**
     * 转换成数组，去掉重复，再组合好。
     * 需 jquery支持
     */
    removeRepeat: function (str) {
        var strArr = str.split(",");
        strArr.sort(); //排序
        var result = $.unique(strArr);
        return result;
    },
    /**
     * 如果字符串是null返回空字符,否则返回空str
     */
    checkNull: function (str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    },
    /**
     * 验证手机号
     */
    checkMobile: function (str) {
        var myreg = /^1[3|4|5|7|8][0-9]{9}$/;
        if (!myreg.test(str)) {
            return false;
        }
        return true;
    },
    /**
     * 验证输入内容是否为数字
     */
    isDigit: function (value) {
        var patrn = /^[0-9]*$/;
        if (!patrn.test(value) || value == "" || value == null || value == "null") {
            return false;
        } else {
            return true;
        }
    },
    /**
     * 验证输入内容是否为大于等于0的正整数,允许为小数
     */
    isNumber: function (value) {
        return /^\d+(\.\d+)?$/.test(value + "");
    },
    /**
     * 验证输入内容是否为大于等于0的正整数
     */
    isPositiveNumber: function (value) {
        return /^[0-9]+$/.test(value + "");
    },
    /**
     * 验证输入内容是否为双精度
     */
    isDouble: function (value) {
        if (value.length != 0) {
            var reg = /^[-\+]?\d+(\.\d+)?$/;
            if (reg.test(value)) {
                return true;
            } else {
                return false;
            }
        }
    },
    /**
     * 验证是否为经度  经度范围：-180.0000~180.0000;
     */
    isLng: function (value) {
        var reg = /^(((\d|[1-9]\d|1[1-7]\d|0)\.\d{0,10})|(\d|[1-9]\d|1[1-7]\d|0{1,3})|180\.0{0,10}|180)$/;
        ;
        return reg.test(value);
    },
    /**
     * 验证是否为纬度  纬度范围：-90.0000~90.0000
     */
    isLat: function (value) {
        var reg = /^([0-8]?\d{1}\.\d{0,10}|90\.0{0,10}|[0-8]?\d{1}|90)$/;
        return reg.test(value);
    },
    /**
     * 验证网址
     */
    checkUrl: function (str) {
        if (str == null || str == "") {
            return false;
        } else {
            var RegUrl = new RegExp();
            RegUrl.compile("^[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\?\/.=]+$");
            if (!RegUrl.test(str)) {
                return false;
            }
        }
        return true;
    },
    /**
     * 验证邮箱
     */
    checkEmail: function (str) {
        var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (!myreg.test(str)) {
            return false;
        }
        return true;
    },
    /**
     * 验证账号，英文字母、数字、下划线组合 //6-20位字符
     * @param str
     */
    checkAccount: function (str) {
        if (str == null || str == "") {
            return false;
        } else {
            var regEx = /\w{6,20}/; // /\w{4,16}/
            if (!regEx.test(str)) {
                return false;
            }
        }
        return true;
    },

    /**
     * 比较时间大小
     * 0:data1>data2
     * 1:data1<data2
     * 2:data1=data2
     */
    dateCompare: function (data1, data2) {
        var d1 = new Date(data1.replace(/\-/g, '/'));
        var d2 = new Date(data2.replace(/\-/g, '/'));
        if (d1 > d2) {
            return 0;
        } else if (d1 < d2) {
            return 1;
        } else if (d1 - d2 == 0) {
            return 2;
        }
    },
    /**
     * 获取当前时间,格式[2015-08-18 12:00:00]
     */
    currentTime: function () {
        var d = new Date();
        return d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + " " + d.getHours() + ':' + d.getMinutes() + ":" + d.getSeconds();
    },
    /**
     * 提取字符串中的所有数字
     */
    getNum: function (text) {
        var spaceInputStr = text.replace(/\s+/g, ""); //先去掉空格
        var filterStr = spaceInputStr.replace(/[^0-9]/ig, " ");  //提取数字,并以空格分割
        var value = filterStr.replace(/\s+/g, ' '); //剔除多余的空格[多个空格转为1个空格]
        return value;
    },
    /**
     * 分割标题得到标签,小于1个字符的将被过滤掉
     */
    splitStrTolabel: function (str) {
        if (str == null || str == "") {
            return "";
        } else {
            var spaceStr = str.replace(/\s+/g, ' '); //剔除多余的空格[多个空格转为1个空格]
            var arrayStr = spaceStr.split(" ");
            var newStr = "";
            for (var i = 0; i < arrayStr.length; i++) {
                if (arrayStr[i].length < 2) {
                    continue;
                }
                newStr.append(arrayStr[i])
            }
            return newStr;
        }
    }

}

