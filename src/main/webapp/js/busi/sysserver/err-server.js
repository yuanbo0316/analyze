/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



var test;
var ss = [];
$("#server", navTab.getCurrentPanel()).change(function() {
    if ($("#server", navTab.getCurrentPanel()).val() == 'jtgzfw') {
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
    } else if ($("#server", navTab.getCurrentPanel()).val() == 'jtgzfw_man') {
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
    } else if ($("#server", navTab.getCurrentPanel()).val() == 'jtaqxh') {
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
    $("#begin-hour,#end-hour", navTab.getCurrentPanel()).append(option);
}

for (var i = 0; i < 60; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-minute,#end-minute,#begin-second,#end-second", navTab.getCurrentPanel()).append(option);
}

initParaSelect("st.server", $("#server", navTab.getCurrentPanel()));
initServiceParaSelect("jtgzfw.service", $("#service_code_detail", navTab.getCurrentPanel()));
initServiceParaSelect("jtgzfw_man.service", $("#service_code_detail", navTab.getCurrentPanel()));
initServiceParaSelect("jtaqxh.service", $("#service_code_detail", navTab.getCurrentPanel()));
$("#service_code_detail", navTab.getCurrentPanel()).combox();

$("#search-button", navTab.getCurrentPanel()).click(function() {
    if ($("#search-form", navTab.getCurrentPanel()).valid()) {
        //%20=" "   %3A=":"
//        var begin = $("#begin", navTab.getCurrentPanel()).val() + "%20" + $("#begin-hour", navTab.getCurrentPanel()).val() + "%3A" + $("#begin-minute", navTab.getCurrentPanel()).val() + "%3A" + $("#begin-second", navTab.getCurrentPanel()).val();
//        var end = $("#end", navTab.getCurrentPanel()).val() + "%20" + $("#end-hour", navTab.getCurrentPanel()).val() + "%3A" + $("#end-minute", navTab.getCurrentPanel()).val() + "%3A" + $("#end-second", navTab.getCurrentPanel()).val();
        var begin = $("#begin", navTab.getCurrentPanel()).val() + " " + $("#begin-hour", navTab.getCurrentPanel()).val() + ":" + $("#begin-minute", navTab.getCurrentPanel()).val() + ":" + $("#begin-second", navTab.getCurrentPanel()).val();
        var end = $("#end", navTab.getCurrentPanel()).val() + " " + $("#end-hour", navTab.getCurrentPanel()).val() + ":" + $("#end-minute", navTab.getCurrentPanel()).val() + ":" + $("#end-second", navTab.getCurrentPanel()).val();
        $("#error_list", navTab.getCurrentPanel()).cutPage({
            page_size: 200,
            begin: begin,
            end: end,
            service_code_detail: $("#service_code_detail", navTab.getCurrentPanel()).val(),
            server: $("#server", navTab.getCurrentPanel()).val(),
            service_code: "S34201"
        }, function(list) {
            for (var i = 0; i < list.length; i++) {
                list[i].service_code = getParaValue(list[i].server + ".service", list[i].service_code);
                list[i].server = getParaValue("st.server", list[i].server);
            }
        });
    }
});

$("#search-today", navTab.getCurrentPanel()).click(function() {
    var begin = getNowDate();
    var end = getNowDateTime();
    $("#error_list", navTab.getCurrentPanel()).cutPage({
        page_size: 200,
        begin: begin,
        end: end,
        service_code_detail: $("#service_code_detail", navTab.getCurrentPanel()).val(),
        server: $("#server", navTab.getCurrentPanel()).val(),
        service_code: "S34201"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].service_code = getParaValue(list[i].server + ".service", list[i].service_code);
            list[i].server = getParaValue("st.server", list[i].server);
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
    if ($("#service_code_detail", navTab.getCurrentPanel()).val() == "" || $("#server", navTab.getCurrentPanel()).val() == "") {
        alertMsg.confirm("监控系统和服务码不能为空");
        return;
    }
    sessionStorage.err_server_detail = JSON.stringify({server: $("#server", navTab.getCurrentPanel()).val(), service_code: $("#service_code_detail", navTab.getCurrentPanel()).val(), begin: begin, end: end});
    $.pdialog.open("page/sysserver/err-server-detail.html", 'err-server-detail', getParaValue($("#server", navTab.getCurrentPanel()).val() + ".service", $("#service_code_detail", navTab.getCurrentPanel()).val()), {"width": 800, "height": 510});
});

$("#search-all-detail", navTab.getCurrentPanel()).click(function() {
    var begin = $("#begin", navTab.getCurrentPanel()).val();
    var end = $("#end", navTab.getCurrentPanel()).val();
    if (begin == "" || end == "") {
        alertMsg.confirm("请设置时间");
        return;
    }
    sessionStorage.err_server_all = JSON.stringify({begin: begin, end: end});
    $.pdialog.open("page/sysserver/err-server-all.html", 'err-server-all', "服务异常统计", {"width": 800, "height": 510});
});

$("#edit", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.err_server_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/sysserver/err-server-dialog.html", 'err-server-dialog', rowData.response_desc, {"width": 800, "height": 610});
    }
});

