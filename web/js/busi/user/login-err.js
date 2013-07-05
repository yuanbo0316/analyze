


initParaSelect("st.server", $("#server", navTab.getCurrentPanel()));
for (var i = 0; i < 24; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-hour,#end-hour", navTab.getCurrentPanel()).append(option);
}

for (var i = 0; i < 60; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-minute,#end-minute,#begin-second,#end-second", navTab.getCurrentPanel()).append(option);
}

$("#search-button", navTab.getCurrentPanel()).click(function() {
    if ($("#search-form", navTab.getCurrentPanel()).valid()) {
        var begin = $("#begin", navTab.getCurrentPanel()).val() + " " + $("#begin-hour", navTab.getCurrentPanel()).val() + ":" + $("#begin-minute", navTab.getCurrentPanel()).val() + ":" + $("#begin-second", navTab.getCurrentPanel()).val();
        var end = $("#end", navTab.getCurrentPanel()).val() + " " + $("#end-hour", navTab.getCurrentPanel()).val() + ":" + $("#end-minute", navTab.getCurrentPanel()).val() + ":" + $("#end-second", navTab.getCurrentPanel()).val();

        $("#error_login_list", navTab.getCurrentPanel()).cutPage({
            page_size: 200,
            ip_err: $("#ip_err", navTab.getCurrentPanel()).val(),
            begin: begin,
            end: end,
            username_err: $("#username_err", navTab.getCurrentPanel()).val(),
            server: $("#server", navTab.getCurrentPanel()).val(),
            service_code: "S34109"}, function(list) {
            for (var i = 0; i < list.length; i++) {
                list[i].server = getParaValue("st.server", list[i].server);
                list[i].from_node = getParaValue("st.node", list[i].from_node);
                list[i].opt = getParaValue("login.opt", list[i].opt);
            }
        });
    }
});

$("#search-today", navTab.getCurrentPanel()).click(function() {
    var begin = getNowDate();
    var end = getNowDateTime();
    $("#error_login_list", navTab.getCurrentPanel()).cutPage({
        page_size: 200,
        ip_err: $("#ip_err", navTab.getCurrentPanel()).val(),
        begin: begin,
        end: end,
        username_err: $("#username_err", navTab.getCurrentPanel()).val(),
        server: $("#server", navTab.getCurrentPanel()).val(),
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

$("#search-detail", navTab.getCurrentPanel()).click(function() {
    var begin = $("#begin", navTab.getCurrentPanel()).val();
    var end = $("#end", navTab.getCurrentPanel()).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    if ( $("#server", navTab.getCurrentPanel()).val() == "") {
        alertMsg.confirm("监控系统不能为空");
        return;
    }
    sessionStorage.log_err_detail = JSON.stringify({server: $("#server", navTab.getCurrentPanel()).val(),begin: begin, end: end,username_err: $("#username_err", navTab.getCurrentPanel()).val(),ip_err: $("#ip_err", navTab.getCurrentPanel()).val()});
    $.pdialog.open("page/user/login-err-detail.html", 'login-err-detail','用户登录失败统计', {"width": 800, "height": 510});
});