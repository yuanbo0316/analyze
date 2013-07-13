/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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
    if ($("#username_traking", navTab.getCurrentPanel()).val() == "" && $("#ip_addr_trake", navTab.getCurrentPanel()).val() == "") {
        alertMsg.confirm("请输入IP或用户名");
        return;
    }
    if ($("#search-form", navTab.getCurrentPanel()).valid()) {
        var begin = $("#begin", navTab.getCurrentPanel()).val() + " " + $("#begin-hour", navTab.getCurrentPanel()).val() + ":" + $("#begin-minute", navTab.getCurrentPanel()).val() + ":" + $("#begin-second", navTab.getCurrentPanel()).val();
        var end = $("#end", navTab.getCurrentPanel()).val() + " " + $("#end-hour", navTab.getCurrentPanel()).val() + ":" + $("#end-minute", navTab.getCurrentPanel()).val() + ":" + $("#end-second", navTab.getCurrentPanel()).val();
        var o = new AjaxOptions();
        o.timeout = 60000;
        o.put("begin", begin);
        o.put("end", end);
        o.put("username_trake", $("#username_traking", navTab.getCurrentPanel()).val());
        o.put("ip_addr", $("#ip_addr_trake").val());
        $("ip_addr_trake", navTab.getCurrentPanel()).remove();
        o.put("service_code", "S34112");
        o.sus = function(data) {
            var result = data.result;
            for (var i = 0; i < result.length; i++) {
                if (result[i].service_code != null)
                    result[i].service_code = getParaValue(result[i].server + ".service", result[i].service_code) + "(" + result[i].service_code + ")";
                result[i].server = getParaValue("st.server", result[i].server);
                result[i].from_node = getParaValue("st.node", result[i].from_node);
                console.log("==============" + result[i].response_code);
                if (result[i].response_code != "" && result[i].service_code == null) {
                    result[i].success = '失败';
                } else if (result[i].service_code == null && result[i].response_code == "") {
                    result[i].success = '成功';
                } else {
                }
            }
            padBackTable(data.result, "#user_trake_list");
        }
        $.ajax(o);
    }
});

$("#search-today", navTab.getCurrentPanel()).click(function() {
    if ($("#username_traking", navTab.getCurrentPanel()).val() == "" && $("#ip_addr_trake", navTab.getCurrentPanel()).val() == "") {
        alertMsg.confirm("请输入IP或用户名");
        return;
    }
    var o = new AjaxOptions();
    o.timeout = 60000;
    o.put("begin", getNowDate());
    o.put("end", getNowDateTime());
    o.put("username_trake", $("#username_traking", navTab.getCurrentPanel()).val());
    o.put("ip_addr", $("#ip_addr_trake").val());
    $("ip_addr_trake", navTab.getCurrentPanel()).remove();
    o.put("service_code", "S34112");
    o.sus = function(data) {
        var result = data.result;
        for (var i = 0; i < result.length; i++) {
            if (result[i].service_code != null)
                result[i].service_code = getParaValue(result[i].server + ".service", result[i].service_code) + "(" + result[i].service_code + ")";
            result[i].server = getParaValue("st.server", result[i].server);
            result[i].from_node = getParaValue("st.node", result[i].from_node);
            if (result[i].response_code != "" && result[i].service_code == null) {
                result[i].success = '失败';
            } else if (result[i].service_code == null && result[i].response_code == "") {
                result[i].success = '成功';
            } else {
            }
        }
        padBackTable(data.result, "#user_trake_list");
    }
    $.ajax(o);

});

$("#edit", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.user_trake_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/user/user-trake-dialog.html", 'user-trake-dialog', rowData.ip + rowData.service_code, {"width": 800, "height": 610});
    }
});
