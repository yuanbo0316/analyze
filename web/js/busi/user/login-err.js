var page = navTab.getCurrentPanel();


initParaSelect("st.server", $("#server", page));
for (var i = 0; i < 24; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-hour,#end-hour", page).append(option);
}

for (var i = 0; i < 60; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-minute,#end-minute,#begin-second,#end-second", page).append(option);
}

$("#search-button", page).click(function() {
    if ($("#search-form", page).valid()) {
        var begin = $("#begin", page).val() + " " + $("#begin-hour", page).val() + ":" + $("#begin-minute", page).val() + ":" + $("#begin-second", page).val();
        var end = $("#end", page).val() + " " + $("#end-hour", page).val() + ":" + $("#end-minute", page).val() + ":" + $("#end-second", page).val();

        $("#error_login_list", page).cutPage({
            page_size: 200,
            ip_err: $("#ip_err", page).val(),
            begin: begin,
            end: end,
            username_err: $("#username_err", page).val(),
            server: $("#server", page).val(),
            service_code: "S34109"}, function(list) {
            for (var i = 0; i < list.length; i++) {
                list[i].server = getParaValue("st.server", list[i].server);
                list[i].from_node = getParaValue("st.node", list[i].from_node);
                list[i].opt = getParaValue("login.opt", list[i].opt);
            }
        });
    }
});

$("#search-today", page).click(function() {
    var begin = getNowDate();
    var end = getNowDateTime();
    $("#error_login_list", page).cutPage({
        page_size: 200,
        ip_err: $("#ip_err", page).val(),
        begin: begin,
        end: end,
        username_err: $("#username_err", page).val(),
        server: $("#server", page).val(),
        service_code: "S34109"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            for (var i = 0; i < list.length; i++) {
                list[i].server = getParaValue("st.server", list[i].server);
                list[i].from_node = getParaValue("st.node", list[i].from_node);
                list[i].opt = getParaValue("login.opt", list[i].opt);
            }
        }
    });
});

$("#search-detail", page).click(function() {
    var begin = $("#begin", page).val();
    var end = $("#end", page).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    if ( $("#server", page).val() == "") {
        alertMsg.confirm("监控系统不能为空");
        return;
    }
    sessionStorage.log_err_detail = JSON.stringify({server: $("#server", page).val(),begin: begin, end: end,username_err: $("#username_err", page).val(),ip_err: $("#ip_err", page).val()});
    $.pdialog.open("page/user/login-err-detail.html", 'login-err-detail','用户登录失败统计', {"width": 800, "height": 510});
});