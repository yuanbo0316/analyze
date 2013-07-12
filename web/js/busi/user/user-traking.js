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
    $("#begin-minute,#end-minute,#begin-second,#end-second", page).append(option);
}

$("#search-button", page).click(function() {
    if ($("#username_traking", page).val() == "" && $("#ip_addr_trake", page).val() == "") {
        alertMsg.confirm("请输入IP或用户名");
        return;
    }
    if ($("#search-form", page).valid()) {
        var begin = $("#begin", page).val() + " " + $("#begin-hour", page).val() + ":" + $("#begin-minute", page).val() + ":" + $("#begin-second", page).val();
        var end = $("#end", page).val() + " " + $("#end-hour", page).val() + ":" + $("#end-minute", page).val() + ":" + $("#end-second", page).val();
        var o = new AjaxOptions();
        o.timeout = 60000;
        o.put("begin", begin);
        o.put("end", end);
        o.put("username_trake", $("#username_traking", page).val());
        o.put("ip_addr", $("#ip_addr_trake").val());
        $("ip_addr_trake", page).remove();
        o.put("service_code", "S34112");
        o.sus = function(data) {
            var result = data.result;
            for (var i = 0; i < result.length; i++) {
                if (result[i].service_code != null)
                    result[i].service_code = getParaValue(result[i].server + ".service", result[i].service_code) + "(" + result[i].service_code + ")";
                result[i].server = getParaValue("st.server", result[i].server);
                result[i].from_node = getParaValue("st.node", result[i].from_node);
                console.log("=============="+result[i].response_code);
                if (result[i].response_code != "") {
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

$("#search-today", page).click(function() {
    if ($("#username_traking", page).val() == "" && $("#ip_addr_trake", page).val() == "") {
        alertMsg.confirm("请输入IP或用户名");
        return;
    }
    var o = new AjaxOptions();
    o.timeout = 60000;
    o.put("begin", getNowDate());
    o.put("end", getNowDateTime());
    o.put("username_trake", $("#username_traking", page).val());
    o.put("ip_addr", $("#ip_addr_trake").val());
    $("ip_addr_trake", page).remove();
    o.put("service_code", "S34112");
    o.sus = function(data) {
        var result = data.result;
        for (var i = 0; i < result.length; i++) {
            if (result[i].service_code != 'undifined')
                result[i].service_code = getParaValue(result[i].server + ".service", result[i].service_code) + "(" + result[i].service_code + ")";
            result[i].server = getParaValue("st.server", result[i].server);
            result[i].from_node = getParaValue("st.node", result[i].from_node);
        }
        padBackTable(data.result, "#user_trake_list");
    }
    $.ajax(o);

});

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.user_trake_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/user/user-trake-dialog.html", 'user-trake-dialog', rowData.ip + rowData.service_code, {"width": 800, "height": 610});
    }
});