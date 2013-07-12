/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var page = navTab.getCurrentPanel();

var test;
var ss = [];
$("#server", page).change(function() {
    if ($("#server", page).val() == 'jtgzfw') {
        test = getParaList("jtgzfw.service");
        for (var i = 0; i < test.length; i++) {
            ss[i] = test[i].col_value;
        }
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
            if ($(this).attr("id") == null || ss.indexOf($(this).attr("id")) != -1) {

            } else {
                $(this).css("display", "none");
            }
        });
    } else if ($("#server", page).val() == 'jtgzfw_man') {
        test = getParaList("jtgzfw_man.service");
        for (var i = 0; i < test.length; i++) {
            ss[i] = test[i].col_value;
        }
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
            if ($(this).attr("id") == null || ss.indexOf($(this).attr("id")) != -1) {

            } else {
                $(this).css("display", "none");
            }
        });
    } else if ($("#server", page).val() == 'jtaqxh') {
        test = getParaList("jtaqxh.service");
        for (var i = 0; i < test.length; i++) {
            ss[i] = test[i].col_value;
        }
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
            if ($(this).attr("id") == null || ss.indexOf($(this).attr("id")) != -1) {

            } else {
                $(this).css("display", "none");
            }
        });
    } else {
   
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");        
        });
    }
});



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

initParaSelect("st.server", $("#server", page));
initServiceParaSelect("jtgzfw.service", $("#service_code_detail", page));
initServiceParaSelect("jtgzfw_man.service", $("#service_code_detail", page));
initServiceParaSelect("jtaqxh.service", $("#service_code_detail", page));
$("#service_code_detail", page).combox();

$("#search-button", page).click(function() {
    if ($("#search-form", page).valid()) {
        //%20=" "   %3A=":"
//        var begin = $("#begin", page).val() + "%20" + $("#begin-hour", page).val() + "%3A" + $("#begin-minute", page).val() + "%3A" + $("#begin-second", page).val();
//        var end = $("#end", page).val() + "%20" + $("#end-hour", page).val() + "%3A" + $("#end-minute", page).val() + "%3A" + $("#end-second", page).val();
        var begin = $("#begin", page).val() + " " + $("#begin-hour", page).val() + ":" + $("#begin-minute", page).val() + ":" + $("#begin-second", page).val();
        var end = $("#end", page).val() + " " + $("#end-hour", page).val() + ":" + $("#end-minute", page).val() + ":" + $("#end-second", page).val();
        $("#error_list", page).cutPage({
            page_size: 200,
            begin: begin,
            end: end,
            service_code_detail: $("#service_code_detail", page).val(),
            server: $("#server", page).val(),
            service_code: "S34201"
        }, function(list) {
            for (var i = 0; i < list.length; i++) {
                list[i].service_code = getParaValue(list[i].server + ".service", list[i].service_code);
                list[i].server = getParaValue("st.server", list[i].server);
            }
        });
    }
});

$("#search-today", page).click(function() {
    var begin = getNowDate();
    var end = getNowDateTime();
    $("#error_list", page).cutPage({
        page_size: 200,
        begin: begin,
        end: end,
        service_code_detail: $("#service_code_detail", page).val(),
        server: $("#server", page).val(),
        service_code: "S34201"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].service_code = getParaValue(list[i].server + ".service", list[i].service_code);
            list[i].server = getParaValue("st.server", list[i].server);
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
    if ($("#service_code_detail", page).val() == "" || $("#server", page).val() == "") {
        alertMsg.confirm("监控系统和服务码不能为空");
        return;
    }
    sessionStorage.err_server_detail = JSON.stringify({server: $("#server", page).val(), service_code: $("#service_code_detail", page).val(), begin: begin, end: end});
    $.pdialog.open("page/sysserver/err-server-detail.html", 'err-server-detail', getParaValue($("#server", page).val() + ".service", $("#service_code_detail", page).val()), {"width": 800, "height": 510});
});

$("#search-all-detail", page).click(function() {
    var begin = $("#begin", page).val();
    var end = $("#end", page).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    sessionStorage.err_server_all = JSON.stringify({begin: begin, end: end});
    $.pdialog.open("page/sysserver/err-server-all.html", 'err-server-all', "服务异常统计", {"width": 800, "height": 510});
});

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.err_server_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/sysserver/err-server-dialog.html", 'err-server-dialog', rowData.response_desc, {"width": 800, "height": 610});
    }
});

