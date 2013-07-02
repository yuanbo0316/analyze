/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var page = $.pdialog.getCurrent();

$("#login", page).click(function() {
    if ($("#login_form", page).valid()) {
        var o = new AjaxOptions($("#login_form", page));
        o.isDialog = true;      //为弹出窗口
        o.put("service_code", "S34001");
        o.sus = function() {
            alertMsg.correct("登录成功！");
            $.pdialog.closeCurrent();
            navTab.reload();      //登录成功后刷新当前navTab
        };
        $.ajax(o);
    }
});

$("#cancel", page).click(function() {
    alertMsg.confirm("确定要退出吗？", {"okCall": function() {
            location.href = "index.html";
        }});
});

$("a.close", page).mousedown(function(e) {
    alertMsg.confirm("确定要退出吗？", {"okCall": function() {
            location.href = "index.html";
        }});
    e.stopPropagation();    //阻止浏览器的冒泡事件
    e.preventDefault();     //阻止浏览器默认的事件
    return false;
});