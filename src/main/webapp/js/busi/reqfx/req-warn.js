/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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

        $("#req_warn_list", navTab.getCurrentPanel()).cutPage({
            page_size: 200,
            req_url: $("#req_url_detail", navTab.getCurrentPanel()).val(),
            begin: begin,
            end: end,
            is_timeout: $("#is_time_out", navTab.getCurrentPanel()).val(),
            server: $("#server", navTab.getCurrentPanel()).val(),
            service_code: "S34301"}, function(list) {
            for (var i = 0; i < list.length; i++) {
                if (list[i].is_timeout == "1") {
                    list[i].is_timeout = "是";
                } else {
                    list[i].is_timeout = "否";
                }
                list[i].server = getParaValue("st.server", list[i].server);
            }
        });
    }
});

$("#search-today", navTab.getCurrentPanel()).click(function() {
    var begin = getNowDate();
    var end = getNowDateTime();
    $("#req_warn_list", navTab.getCurrentPanel()).cutPage({
        page_size: 200,
        req_url: $("#req_url_detail", navTab.getCurrentPanel()).val(),
        begin: begin,
        end: end,
        is_timeout: $("#is_time_out", navTab.getCurrentPanel()).val(),
        server: $("#server", navTab.getCurrentPanel()).val(),
        service_code: "S34301"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            for (var i = 0; i < list.length; i++) {
                if (list[i].is_timeout == "1") {
                    list[i].is_timeout = "是";
                } else {
                    list[i].is_timeout = "否";
                }
                list[i].server = getParaValue("st.server", list[i].server);
            }
        }
    });
});

$("#search-all-detail", navTab.getCurrentPanel()).click(function() {
    var begin = $("#begin", navTab.getCurrentPanel()).val();
    var end = $("#end", navTab.getCurrentPanel()).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    sessionStorage.err_server_all = JSON.stringify({server: $("#server", navTab.getCurrentPanel()).val(), is_timeout: $("#is_time_out", navTab.getCurrentPanel()).val(), end: end, begin: begin});
    $.pdialog.open("page/reqfx/req-warn-all.html", 'req-warn-all', "请求异常统计", {"width": 800, "height": 510});
});

$("#edit", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.req_warn_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/reqfx/req-warn-dialog.html", 'req-warn-dialog', rowData.req_url, {"width": 800, "height": 600});
    }
});