/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function pic_req_warn_run(){
    /* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var page = navTab.getCurrentPanel();

for (var i = 0; i < 24; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-hour,#end-hour", page).append(option);
}

for (var i = 0; i < 60; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-minute,#end-minute,#begin-second,#end-second",page).append(option);
}

$("#search-button", page).click(function() {
    if ($("#search-form", page).valid()) {
        var begin = $("#begin", page).val() + " " + $("#begin-hour", page).val() + ":" + $("#begin-minute",page).val() + ":" + $("#begin-second", page).val();
        var end = $("#end", page).val() + " " + $("#end-hour",page).val() + ":" + $("#end-minute",page).val() + ":" + $("#end-second",page).val();

        $("#req_warn_list",page).cutPage({
            page_size: 200,
            begin: begin,
            end: end,
            is_timeout: $("#is_time_out", page).val(),
            service_code: "S34303"}, function(list) {
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

$("#search-today", page).click(function() {
    var begin = getNowDate();
    var end = getNowDateTime();
    $("#req_warn_list", page).cutPage({
        page_size: 200,
        begin: begin,
        end: end,
        is_timeout: $("#is_time_out",page).val(),
        service_code: "S34303"
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

$("#search-all-detail", page).click(function() {
    var begin = $("#begin", page).val();
    var end = $("#end", page).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    sessionStorage.pic_req_warn_all = JSON.stringify({end: end, begin: begin});
    $.pdialog.open("page/reqfx/pic-req-warn-all.html", 'pic-req-warn-all', "照片请求执行时间统计", {"width": 800, "height": 510});
});

$("#pic_sus_fail", page).click(function() {
    var begin = $("#begin", page).val();
    var end = $("#end", page).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    sessionStorage.pic_req_sus_fail = JSON.stringify({end: end, begin: begin});
    $.pdialog.open("page/reqfx/pic-req-sus-fail.html", 'pic-req-sus-fail', "请求成功率", {"width": 800, "height": 510});
});

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.pic_req_warn_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/reqfx/pic-req-warn-dialog.html", 'pic-req-warn-dialog', rowData.xh, {"width": 800, "height": 600});
    }
});
}

pic_req_warn_run();