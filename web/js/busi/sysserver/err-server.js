/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var page = navTab.getCurrentPanel();

$("#server", page).change(function() {
    if ($("#server", page).val() == 'jtgzfw') {
        $("#service_code_detail", page).html(" <option value='' selected='selected'>全部</option>");
        initServiceParaSelect("jtgzfw.service", $("#service_code_detail", page));
    } else if ($("#server", page).val() == 'jtgzfw_man') {
        $("#service_code_detail", page).html(" <option value='' selected='selected'>全部</option>");
        initServiceParaSelect("jtgzfw_man.service", $("#service_code_detail", page));
    } else if ($("#server", page).val() == 'jtaqxh') {
        $("#service_code_detail", page).html(" <option value='' selected='selected'>全部</option>");
        initServiceParaSelect("jtaqxh.service", $("#service_code_detail", page));
    } else {
        $("#service_code_detail", page).html(" <option value='' selected='selected'>全部</option>");
        initServiceParaSelect("jtgzfw.service", $("#service_code_detail", page));
        initServiceParaSelect("jtgzfw_man.service", $("#service_code_detail", page));
        initServiceParaSelect("jtaqxh.service", $("#service_code_detail", page));
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

$("#search-button", page).click(function() {
    if ($("#search-form", page).valid()) {
        //%20=" "   %3A=":"
//        var begin = $("#begin", page).val() + "%20" + $("#begin-hour", page).val() + "%3A" + $("#begin-minute", page).val() + "%3A" + $("#begin-second", page).val();
//        var end = $("#end", page).val() + "%20" + $("#end-hour", page).val() + "%3A" + $("#end-minute", page).val() + "%3A" + $("#end-second", page).val();
        var begin = $("#begin", page).val() + " " + $("#begin-hour", page).val() + ":" + $("#begin-minute", page).val() + ":" + $("#begin-second", page).val();
        var end = $("#end", page).val() + " " + $("#end-hour", page).val() + ":" + $("#end-minute", page).val() + ":" + $("#end-second", page).val();
        $("#error_list", page).cutPage({
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
    $("#error_list", page).cutPage({
        begin: getNowDate(),
        end: getNowDateTime(),
        service_code_detail: $("#service_code_detail", page).val(),
        server: $("#server", page).val(),
        service_code: "S34201"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {            
            list[i].service_code = getParaValue(list[i].server + ".service", list[i].service_code);
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });
}).trigger("click");

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.err_server_dialog = JSON.stringify(rowData);
        $.pdialog.open("page/sysserver/err-server-dialog.html", 'err-server-dialog', rowData.response_desc, {"width": 800, "height": 420});
    }
});

