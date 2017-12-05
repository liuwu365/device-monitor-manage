/**
 * Created by rocking on 2016/7/26 0026.
 * 加载下拉框
 */
var _LoadSelectBox = {
    /**
     * 加载菜单栏
     */
    getParendMenu: function () {
        $.ajax({
            type: "get",
            url: "/base/resources/findParentMenuAll?parentId=0",
            dataType: "json",
            success: function (res) {
                if (res != null && res.code == 200) {
                    var menuId = $("input[name='menuId']").val();
                    var data = res.t;
                    for (var i in data) {
                        if (menuId == data[i].id) {
                            $(".parentMenuBox").append("<option selected=\"true\" value=\"" + data[i].id + "\">" + data[i].name + "</option>");
                        } else {
                            $(".parentMenuBox").append("<option value=\"" + data[i].id + "\">" + data[i].name + "</option>");
                        }
                    }
                }
            }
        });
    }


};
